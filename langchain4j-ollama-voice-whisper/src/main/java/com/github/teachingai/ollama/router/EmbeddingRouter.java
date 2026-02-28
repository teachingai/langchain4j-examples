package com.github.teachingai.ollama.router;

import com.github.teachingai.ollama.config.AppConfig;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Map;

public class EmbeddingRouter {

    public static void register(Javalin app, AppConfig config) {
        // 创建嵌入模型
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getEmbeddingModel())
                .build();

        // GET /V1/embedding
        app.get("/V1/embedding", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                String message = ctx.queryParam("message");
                if (message == null || message.isEmpty()) {
                    message = "Tell me a joke";
                }
                
                Embedding embedding = embeddingModel.embed(message).content();
                List<Double> embeddingValues = embedding.vectorAsList();
                
                ctx.json(Map.of("embeddings", embeddingValues));
            }
        });
    }
}


