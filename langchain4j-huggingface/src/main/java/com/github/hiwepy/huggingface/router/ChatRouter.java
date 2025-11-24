package com.github.hiwepy.huggingface.router;

import com.github.hiwepy.huggingface.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.output.Response;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ChatRouter {

    private static final Logger log = LoggerFactory.getLogger(ChatRouter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void register(Javalin app, AppConfig config) {
        // 创建聊天模型
        ChatLanguageModel chatModel = HuggingFaceChatModel.builder()
                .accessToken(config.getHuggingFaceAccessToken())
                .modelId(config.getChatModel())
                .waitForModel(true)
                .build();

        // GET /v1/generate
        app.get("/v1/generate", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                String message = ctx.queryParam("message");
                if (message == null || message.isEmpty()) {
                    message = "Tell me a joke";
                }
                String response = chatModel.generate(message);
                ctx.json(Map.of("generation", response));
            }
        });
    }
}

