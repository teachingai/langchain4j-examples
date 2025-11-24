package com.github.teachingai.ollama.router;

import com.github.teachingai.ollama.config.AppConfig;
import com.github.teachingai.ollama.service.EmbeddingService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EmbeddingRouter {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingRouter.class);

    public static void register(Javalin app, AppConfig config) {
        // 创建嵌入模型
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .modelName(config.getEmbeddingModel())
                .build();

        EmbeddingService embeddingService = new EmbeddingService(embeddingModel);

        // GET /v1/embedding
        app.get("/v1/embedding", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                String text = ctx.queryParam("text");
                if (text == null || text.isEmpty()) {
                    ctx.status(400).json(Map.of("error", "text parameter is required"));
                    return;
                }
                Map<String, Object> result = embeddingService.embedding(text);
                ctx.json(result);
            }
        });

        // POST /v1/embedding (文件上传)
        app.post("/v1/embedding", new Handler() {
            @Override
            public void handle(Context ctx) throws Exception {
                try {
                    UploadedFile file = ctx.uploadedFile("file");
                    if (file == null) {
                        ctx.status(400).json(Map.of("error", "file parameter is required"));
                        return;
                    }

                    // 将上传的文件保存到临时文件
                    Path tempFile = Files.createTempFile("embedding-", "-" + file.filename());
                    try (InputStream inputStream = file.content();
                         FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
                        inputStream.transferTo(outputStream);
                    }

                    // 处理文件
                    List<Map<String, Object>> result = embeddingService.embedding(tempFile.toFile());
                    ctx.json(result);
                } catch (Exception e) {
                    log.error("Error processing file embedding", e);
                    ctx.status(500).json(Map.of("error", e.getMessage()));
                }
            }
        });
    }
}

