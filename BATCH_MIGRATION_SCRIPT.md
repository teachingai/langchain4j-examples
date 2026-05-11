# 批量迁移脚本指南

## 已完成的模块迁移

1. ✅ `langchain4j-ollama-agents` - 完整迁移
2. ✅ `langchain4j-openai` - 完整迁移

## 迁移步骤

对于每个模块，需要执行以下步骤：

### 1. 更新 pom.xml

**移除的依赖：**
- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-boot-starter-log4j2`
- `spring-boot-starter-undertow` / `spring-boot-starter-tomcat` / `spring-boot-starter-jetty`
- `spring-boot-starter-test`
- `spring-boot-devtools`
- `spring-boot-maven-plugin`
- `langchain4j-*-spring-boot-starter` (改为使用 `langchain4j-*`)

**添加的依赖：**
- `javalin-bundle`
- `javalin-testtools` (test scope)

**更新 build 插件：**
- 移除 `spring-boot-maven-plugin`
- 添加 `maven-shade-plugin` 配置

### 2. 创建新的 Application 类

替换 `@SpringBootApplication` 类为 Javalin 启动类：

```java
package io.github.partmeai.{module};

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Application {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.jsonMapper(new JavalinJackson());
            javalinConfig.showJavalinBanner = false;
        });
        
        // 注册路由
        // Router.register(app, config);
        
        app.start(8080);
    }
}
```

### 3. 创建 AppConfig 类

从 `application.properties` 读取配置：

```java
package io.github.partmeai.{module}.config;

import java.util.Properties;

public class AppConfig {
    private final Properties properties;
    
    public AppConfig(Properties properties) {
        this.properties = properties;
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // 添加特定配置的 getter 方法
}
```

### 4. 迁移 Controller 到 Router

将 `@RestController` 改为 Javalin Router：

```java
package io.github.partmeai.{module}.router;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ChatRouter {
    public static void register(Javalin app, AppConfig config) {
        // 手动创建模型实例
        ChatLanguageModel model = Model.builder()
            .baseUrl(config.getBaseUrl())
            .apiKey(config.getApiKey())
            .build();
        
        app.get("/v1/generate", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                // 处理逻辑
            }
        });
    }
}
```

### 5. 更新 application.properties

- 添加 `server.port=8080`
- 移除 Spring 相关配置（如 `springdoc.*`）

### 6. 删除 Spring 相关文件

- 删除 `@SpringBootApplication` 类
- 删除 `WebConfig`, `SwaggerConfig` 等 Spring 配置类
- 删除旧的 Controller 类（已迁移到 Router）

## 需要迁移的模块列表

根据模块类型，迁移模式略有不同：

### Ollama 相关模块（使用 OllamaChatModel）
- langchain4j-ollama-generation
- langchain4j-ollama-embedding
- langchain4j-ollama-prompt
- langchain4j-ollama-tools
- langchain4j-ollama-local-model
- langchain4j-ollama-fine-tuning
- langchain4j-ollama-rag-* (所有 RAG 模块)
- langchain4j-ollama-voice-* (所有语音模块)
- langchain4j-ollama-observation-* (所有观察模块)

### 其他 AI 提供商模块
- langchain4j-anthropic
- langchain4j-azure-openai
- langchain4j-bedrockai
- langchain4j-chatglm
- langchain4j-coze
- langchain4j-dashscope
- langchain4j-huaweiai-*
- langchain4j-huggingface
- langchain4j-llmsfreeapi
- langchain4j-localai
- langchain4j-minimax
- langchain4j-mistralai
- langchain4j-moonshotai
- langchain4j-qianfan
- langchain4j-vertexai
- langchain4j-vertexai-gemini
- langchain4j-workersai
- langchain4j-zhipuai
- langchain4j-stepfun
- langchain4j-sensenova

### 项目模块
- langchain4j-project-naming
- langchain4j-project-sql

## 注意事项

1. **数据库连接**：对于使用数据库的模块（如 project-sql, project-naming），需要手动管理数据库连接，不再使用 Spring JDBC
2. **依赖注入**：所有依赖都需要手动创建和传递，不再使用 `@Autowired`
3. **配置管理**：使用 Properties 文件手动读取配置
4. **测试**：更新测试代码，使用 Javalin 测试工具

## 快速迁移命令

可以使用以下命令批量查找需要迁移的文件：

```bash
# 查找所有使用 Spring Boot 的 pom.xml
find . -name "pom.xml" -exec grep -l "spring-boot" {} \;

# 查找所有 Spring Boot Application 类
find . -name "*Application.java" -exec grep -l "@SpringBootApplication" {} \;

# 查找所有 Controller
find . -name "*Controller.java" -exec grep -l "@RestController\|@Controller" {} \;
```

