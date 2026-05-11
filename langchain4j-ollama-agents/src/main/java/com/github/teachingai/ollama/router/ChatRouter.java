package io.github.partmeai.ollama.router;

import io.github.partmeai.ollama.config.AppConfig;
import io.github.partmeai.ollama.request.ApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.StreamingResponseHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatRouter {

    private static final Logger log = LoggerFactory.getLogger(ChatRouter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void register(Javalin app, AppConfig config) {
        // 创建聊天模型
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getChatModel())
                .temperature(config.getTemperature())
                .build();

        StreamingChatLanguageModel streamingChatModel = OllamaStreamingChatModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getChatModel())
                .temperature(config.getTemperature())
                .build();

        // GET /V1/generate
        app.get("/V1/generate", new Handler() {
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

        // GET /V1/prompt
        app.get("/V1/prompt", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                String message = ctx.queryParam("message");
                if (message == null || message.isEmpty()) {
                    message = "Tell me a joke";
                }
                String response = chatModel.generate(message);
                ctx.json(List.of(Map.of("text", response)));
            }
        });

        // POST /V1/chat/completions
        app.post("/V1/chat/completions", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
            try {
                ApiRequest.ChatCompletionRequest chatRequest = ctx.bodyAsClass(ApiRequest.ChatCompletionRequest.class);
                
                log.debug("Received chat request: {}", chatRequest);

                List<ChatMessage> messages = chatRequest.messages().stream().map(msg -> {
                    switch (msg.role()) {
                        case ASSISTANT:
                            return new AiMessage(msg.content());
                        case SYSTEM:
                            return new SystemMessage(msg.content());
                        default:
                            return new UserMessage(msg.content());
                    }
                }).collect(Collectors.toList());

                // 检查是否需要流式响应
                Boolean stream = chatRequest.stream();
                if (stream != null && stream) {
                    // 流式响应
                    ctx.contentType("text/event-stream");
                    ctx.header("Cache-Control", "no-cache");
                    ctx.header("Connection", "keep-alive");
                    
                    streamingChatModel.generate(messages, new StreamingResponseHandler(ctx));
                } else {
                    // 同步响应
                    Response<AiMessage> response = chatModel.generate(messages);
                    ctx.json(Map.of(
                            "id", "chat-" + System.currentTimeMillis(),
                            "object", "chat.completion",
                            "created", System.currentTimeMillis() / 1000,
                            "model", chatRequest.model() != null ? chatRequest.model() : config.getChatModel(),
                            "choices", List.of(Map.of(
                                    "index", 0,
                                    "message", Map.of(
                                            "role", "assistant",
                                            "content", response.content().text()
                                    ),
                                    "finish_reason", "stop"
                            )),
                            "usage", Map.of(
                                    "prompt_tokens", response.tokenUsage() != null ? response.tokenUsage().inputTokenCount() : 0,
                                    "completion_tokens", response.tokenUsage() != null ? response.tokenUsage().outputTokenCount() : 0,
                                    "total_tokens", response.tokenUsage() != null ? response.tokenUsage().totalTokenCount() : 0
                            )
                    ));
                }
            } catch (Exception e) {
                log.error("Error processing chat completion request", e);
                ctx.status(500).json(Map.of("error", e.getMessage()));
            }
        });
    }

    private static class StreamingResponseHandler implements StreamingResponseHandler<AiMessage> {
        private final Context ctx;
        private final StringBuilder fullResponse = new StringBuilder();

        public StreamingResponseHandler(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onNext(String token) {
            try {
                fullResponse.append(token);
                Map<String, Object> chunk = Map.of(
                        "id", "chat-" + System.currentTimeMillis(),
                        "object", "chat.completion.chunk",
                        "created", System.currentTimeMillis() / 1000,
                        "model", "ollama",
                        "choices", List.of(Map.of(
                                "index", 0,
                                "delta", Map.of("content", token),
                                "finish_reason", null
                        ))
                );
                ctx.result("data: " + objectMapper.writeValueAsString(chunk) + "\n\n");
            } catch (Exception e) {
                log.error("Error writing streaming chunk", e);
            }
        }

        @Override
        public void onComplete(Response<AiMessage> response) {
            try {
                // 发送完成消息
                Map<String, Object> doneChunk = Map.of(
                        "id", "chat-" + System.currentTimeMillis(),
                        "object", "chat.completion.chunk",
                        "created", System.currentTimeMillis() / 1000,
                        "model", "ollama",
                        "choices", List.of(Map.of(
                                "index", 0,
                                "delta", Map.of(),
                                "finish_reason", "stop"
                        ))
                );
                ctx.result("data: " + objectMapper.writeValueAsString(doneChunk) + "\n\n");
                ctx.result("data: [DONE]\n\n");
            } catch (Exception e) {
                log.error("Error completing stream", e);
            }
        }

        @Override
        public void onError(Throwable error) {
            log.error("Error in streaming response", error);
            try {
                ctx.result("data: " + objectMapper.writeValueAsString(Map.of("error", error.getMessage())) + "\n\n");
            } catch (Exception e) {
                log.error("Error writing error message", e);
            }
        }
    }
}


