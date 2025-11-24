# Langchain4j + Javalin 迁移进度

## ✅ 已完成的模块迁移

### 1. langchain4j-ollama-agents
- ✅ 更新 pom.xml（移除 Spring Boot，添加 Javalin）
- ✅ 创建 Application 类（Javalin 启动）
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 ChatController → ChatRouter
- ✅ 迁移 EmbeddingController → EmbeddingRouter
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

### 2. langchain4j-openai
- ✅ 更新 pom.xml
- ✅ 创建 Application 类
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 ChatController → ChatRouter
- ✅ 迁移 ImageGenController → ImageGenRouter
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

### 3. langchain4j-ollama-generation
- ✅ 更新 pom.xml
- ✅ 创建 Application 类
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 ChatController → ChatRouter
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

### 4. langchain4j-ollama-embedding
- ✅ 更新 pom.xml
- ✅ 创建 Application 类
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 EmbeddingController → EmbeddingRouter
- ✅ 迁移 EmbeddingService（从 Spring AI 改为 langchain4j）
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

### 5. langchain4j-ollama-tools
- ✅ 更新 pom.xml
- ✅ 创建 Application 类
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 ChatController → ChatRouter
- ✅ 迁移 FunctionConfig（移除 Spring 注解）
- ✅ 更新 GetWeatherFunction（移除 Spring RestClient，使用 Java HttpClient）
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

### 6. langchain4j-ollama-prompt
- ✅ 更新 pom.xml
- ✅ 创建 Application 类
- ✅ 创建 AppConfig 配置类
- ✅ 迁移 ChatController → ChatRouter（使用 langchain4j PromptTemplate）
- ✅ 迁移 EmbeddingController → EmbeddingRouter
- ✅ 更新 application.properties
- ✅ 删除 Spring Boot 相关文件

## 📋 待迁移的模块列表

### Ollama 相关模块
- [x] langchain4j-ollama-prompt
- [x] langchain4j-ollama-tools
- [ ] langchain4j-ollama-local-model
- [ ] langchain4j-ollama-fine-tuning
- [ ] langchain4j-ollama-rag-cassandra
- [ ] langchain4j-ollama-rag-chroma
- [ ] langchain4j-ollama-rag-es
- [ ] langchain4j-ollama-rag-milvus
- [ ] langchain4j-ollama-rag-mongodb
- [ ] langchain4j-ollama-rag-neo4j
- [ ] langchain4j-ollama-rag-opensearch
- [ ] langchain4j-ollama-rag-pgvector
- [ ] langchain4j-ollama-rag-pinecone
- [ ] langchain4j-ollama-rag-qdrant
- [ ] langchain4j-ollama-rag-redis
- [ ] langchain4j-ollama-rag-weaviate
- [ ] langchain4j-ollama-voice-chattts
- [ ] langchain4j-ollama-voice-edgetts
- [ ] langchain4j-ollama-voice-emoti
- [ ] langchain4j-ollama-voice-whisper
- [ ] langchain4j-ollama-voice-assistant
- [ ] langchain4j-ollama-observation-langfuse
- [ ] langchain4j-ollama-observation-prometheus

### 其他 AI 提供商模块
- [ ] langchain4j-anthropic
- [ ] langchain4j-azure-openai
- [ ] langchain4j-bedrockai
- [ ] langchain4j-chatglm
- [ ] langchain4j-coze
- [ ] langchain4j-dashscope
- [ ] langchain4j-huaweiai-gallery
- [ ] langchain4j-huaweiai-pangu
- [ ] langchain4j-huggingface
- [ ] langchain4j-llmsfreeapi
- [ ] langchain4j-localai
- [ ] langchain4j-minimax
- [ ] langchain4j-mistralai
- [ ] langchain4j-moonshotai
- [ ] langchain4j-qianfan
- [ ] langchain4j-vertexai
- [ ] langchain4j-vertexai-gemini
- [ ] langchain4j-workersai
- [ ] langchain4j-zhipuai
- [ ] langchain4j-stepfun
- [ ] langchain4j-sensenova

### 项目模块
- [ ] langchain4j-project-naming
- [ ] langchain4j-project-sql

## 🔄 迁移模式

所有模块都遵循相同的迁移模式：

1. **更新 pom.xml**
   - 移除所有 Spring Boot 依赖
   - 移除 `langchain4j-*-spring-boot-starter`
   - 添加 `langchain4j-*`（非 starter）
   - 添加 `javalin-bundle`
   - 更新 build 插件（使用 maven-shade-plugin）

2. **创建 Application 类**
   - 替换 `@SpringBootApplication` 为 Javalin 启动类
   - 手动加载配置
   - 注册路由

3. **创建 AppConfig 类**
   - 从 Properties 文件读取配置
   - 提供配置 getter 方法

4. **迁移 Controller → Router**
   - 移除 `@RestController` 注解
   - 使用 Javalin Handler
   - 手动创建模型实例

5. **更新 Service 层**
   - 移除 `@Service` 注解
   - 移除 `@Autowired` 依赖注入
   - 通过构造函数手动注入依赖

6. **更新配置文件**
   - 添加 `server.port=8080`
   - 移除 Spring 相关配置

7. **删除 Spring 文件**
   - 删除 `@SpringBootApplication` 类
   - 删除 `WebConfig`, `SwaggerConfig` 等配置类

## 📝 注意事项

1. **数据库连接**：对于使用数据库的模块（project-sql, project-naming），需要手动管理 JDBC 连接
2. **文件上传**：使用 Javalin 的 `ctx.uploadedFile()` 替代 Spring 的 `MultipartFile`
3. **流式响应**：使用 langchain4j 的 `StreamingResponseHandler` 替代 Reactor 的 `Flux`
4. **文档解析**：使用 langchain4j 的文档加载器和解析器替代 Spring AI 的实现

## 🚀 快速迁移命令

可以使用以下命令批量查找需要迁移的文件：

```bash
# 查找所有使用 Spring Boot 的 pom.xml
find . -name "pom.xml" -exec grep -l "spring-boot" {} \;

# 查找所有 Spring Boot Application 类
find . -name "*Application.java" -exec grep -l "@SpringBootApplication" {} \;

# 查找所有 Controller
find . -name "*Controller.java" -exec grep -l "@RestController\|@Controller" {} \;
```

