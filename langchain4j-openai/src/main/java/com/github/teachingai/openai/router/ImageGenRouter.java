package com.github.teachingai.openai.router;

import com.github.teachingai.openai.config.AppConfig;
import com.github.teachingai.openai.request.ImageGenRequest;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.net.URI;
import java.util.Map;

public class ImageGenRouter {

    public static void register(Javalin app, AppConfig config) {
        // 创建图像生成模型
        ImageModel imageModel = OpenAiImageModel.builder()
                .baseUrl(config.getOpenAiBaseUrl())
                .apiKey(config.getOpenAiApiKey())
                .build();

        // POST /imagegen
        app.post("/imagegen", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                try {
                    ImageGenRequest request = ctx.bodyAsClass(ImageGenRequest.class);
                    Response<Image> response = imageModel.generate(request.prompt());
                    URI imageUrl = response.content().url();
                    ctx.redirect(imageUrl.toString());
                } catch (Exception e) {
                    ctx.status(500).json(Map.of("error", e.getMessage()));
                }
            }
        });
    }
}

