package com.github.teachingai.ollama;

import com.github.teachingai.ollama.config.AppConfig;
import com.github.teachingai.ollama.router.ChatRouter;
import com.github.teachingai.ollama.router.EmbeddingRouter;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // 加载配置
        AppConfig config = loadConfig();
        
        // 创建 Javalin 应用
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.jsonMapper(new JavalinJackson());
            javalinConfig.showJavalinBanner = false;
        });

        // 注册路由
        ChatRouter.register(app, config);
        EmbeddingRouter.register(app, config);

        // 启动服务器
        int port = Integer.parseInt(config.getProperty("server.port", "8080"));
        app.start(port);
        log.info("Langchain4j Ollama Voice EdgeTTS Application started on port {}", port);
    }

    private static AppConfig loadConfig() {
        Properties props = new Properties();
        try (InputStream is = Application.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception e) {
            log.warn("Failed to load application.properties, using defaults", e);
        }
        return new AppConfig(props);
    }
}


