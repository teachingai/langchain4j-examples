package io.github.partmeai.aisql.router;

import io.github.partmeai.aisql.config.AppConfig;
import io.github.partmeai.aisql.exception.SqlGenerationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlRouter {

    private static final Logger log = LoggerFactory.getLogger(SqlRouter.class);
    private static DataSource dataSource;
    private static String schema;
    private static String sqlPromptTemplate;

    public static void register(Javalin app, AppConfig config) {
        // 加载schema和prompt模板
        loadResources();
        
        // 初始化数据源
        initDataSource(config);

        // 创建聊天模型
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getChatModel())
                .temperature(config.getTemperature())
                .build();

        // POST /sql
        app.post("/sql", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                try {
                    SqlRequest sqlRequest = ctx.bodyAsClass(SqlRequest.class);
                    
                    // 构建提示词
                    String prompt = sqlPromptTemplate
                            .replace("{question}", sqlRequest.question())
                            .replace("{ddl}", schema);
                    
                    // 调用AI生成SQL
                    Response<String> response = chatModel.generate(
                            new SystemMessage("You are a SQL expert. Generate only SELECT queries."),
                            new UserMessage(prompt)
                    );
                    
                    String query = response.content().trim();
                    
                    // 只允许SELECT查询
                    if (!query.toLowerCase().startsWith("select")) {
                        throw new SqlGenerationException(query);
                    }
                    
                    // 执行查询
                    List<Map<String, Object>> results = executeQuery(query);
                    
                    ctx.json(new Answer(query, results));
                } catch (SqlGenerationException e) {
                    log.error("SQL generation error", e);
                    ctx.status(400).json(Map.of("error", "Only SELECT queries are supported. Generated: " + e.getMessage()));
                } catch (Exception e) {
                    log.error("Error processing SQL request", e);
                    ctx.status(500).json(Map.of("error", e.getMessage()));
                }
            }
        });
    }

    private static void initDataSource(AppConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getJdbcUsername());
        hikariConfig.setPassword(config.getJdbcPassword());
        hikariConfig.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(hikariConfig);
        
        // 初始化数据库
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // 执行schema.sql
            if (schema != null && !schema.isEmpty()) {
                // 分割SQL语句并执行
                String[] statements = schema.split(";");
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                }
            }
            
            // 加载data.sql
            try (InputStream is = SqlRouter.class.getClassLoader()
                    .getResourceAsStream("data.sql")) {
                if (is != null) {
                    String dataSql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    String[] statements = dataSql.split(";");
                    for (String sql : statements) {
                        sql = sql.trim();
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize database", e);
        }
    }

    private static void loadResources() {
        try {
            // 加载schema.sql
            try (InputStream is = SqlRouter.class.getClassLoader()
                    .getResourceAsStream("schema.sql")) {
                if (is != null) {
                    schema = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
            
            // 加载sql-prompt-template.st
            try (InputStream is = SqlRouter.class.getClassLoader()
                    .getResourceAsStream("sql-prompt-template.st")) {
                if (is != null) {
                    sqlPromptTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                } else {
                    // 使用默认模板
                    sqlPromptTemplate = "Given the DDL in the DDL section, write an SQL query to answer the question in the QUESTION section.\n" +
                            "Only produce select queries. If the question would result in an insert, update,\n" +
                            "or delete, or if the query would alter the DDL in any way, say that the operation\n" +
                            "isn't supported. If the question can't be answered, say that the DDL doesn't support\n" +
                            "answering that question.\n\n\n" +
                            "QUESTION\n{question}\n\n" +
                            "DDL\n{ddl}";
                }
            }
        } catch (IOException e) {
            log.error("Failed to load resources", e);
        }
    }

    private static List<Map<String, Object>> executeQuery(String query) throws Exception {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        }
        return results;
    }

    public record Answer(String sqlQuery, List<Map<String, Object>> results) {}

    public record SqlRequest(String question) {}
}

