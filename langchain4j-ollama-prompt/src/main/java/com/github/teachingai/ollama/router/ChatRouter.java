package com.github.teachingai.ollama.router;

import com.github.teachingai.ollama.config.AppConfig;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Map;

public class ChatRouter {

    public static void register(Javalin app, AppConfig config) {
        // 创建聊天模型
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getChatModel())
                .temperature(config.getTemperature())
                .build();

        // GET /v1/prompt
        app.get("/v1/prompt", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                String message = ctx.queryParam("message");
                if (message == null || message.isEmpty()) {
                    message = "Tell me a joke";
                }
                
                PromptTemplate promptTemplate = PromptTemplate.from("Tell me a {adjective} joke about {topic}");
                Prompt prompt = promptTemplate.apply(Map.of("adjective", "funny", "topic", "cats"));
                
                Response<String> response = chatModel.generate(prompt.text());
                ctx.json(List.of(Map.of("text", response.content())));
            }
        });
    }
}

