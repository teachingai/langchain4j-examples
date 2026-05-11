package io.github.partmeai.ollama.service;

import com.alibaba.fastjson2.JSONObject;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Embedding Service
 */
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    /**
     * Embed text
     *
     * @param text text to embed
     * @return embeddings
     */
    public Map<String, Object> embedding(String text) {
        Embedding embedding = embeddingModel.embed(text).content();
        List<Double> embeddingValues = embedding.vectorAsList();
        return Map.of("embeddings", embeddingValues);
    }

    /**
     * Embed Document File
     *
     * @param file file to embed
     * @return embeddings
     */
    public List<Map<String, Object>> embedding(File file) {
        // 1. 解析 PDF 文件
        DocumentParser documentParser = new ApachePdfBoxDocumentParser();
        Document document = FileSystemDocumentLoader.loadDocument(file.toPath(), documentParser);

        // 2. 分割文档为段落（每页一个文档）
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);

        // 3. 为每个段落生成嵌入
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment.text()).content();
            List<Double> embeddingValues = embedding.vectorAsList();
            
            mapList.add(JSONObject.of(
                    "id", UUID.randomUUID().toString(),
                    "embedding", embeddingValues,
                    "content", segment.text(),
                    "metadata", segment.metadata().toMap()
            ));
        }
        return mapList;
    }
}

