package io.github.partmeai.ollama.config;

import java.util.Properties;

public class AppConfig {
    private final Properties properties;

    public AppConfig(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getOllamaBaseUrl() {
        return getProperty("langchain4j.ollama.base-url", "http://localhost:11434");
    }

    public String getChatModel() {
        return getProperty("langchain4j.ollama.chat.options.model", "qwen:7b");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.ollama.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}

