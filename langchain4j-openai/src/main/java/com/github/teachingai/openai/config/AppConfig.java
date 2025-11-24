package com.github.teachingai.openai.config;

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

    public String getOpenAiBaseUrl() {
        return getProperty("langchain4j.open-ai.chat-model.base-url", "https://api.openai.com/v1");
    }

    public String getOpenAiApiKey() {
        return getProperty("langchain4j.open-ai.chat-model.api-key", System.getenv("OPENAI_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.open-ai.chat-model.model-name", "gpt-3.5-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.open-ai.chat-model.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}

