package com.github.teachingai.config;

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

    public String getWorkersAiApiKey() {
        return getProperty("langchain4j.workersai.api-key", System.getenv("WORKERSAI_API_KEY"));
    }

    public String getWorkersAiAccountId() {
        return getProperty("langchain4j.workersai.account-id", System.getenv("WORKERSAI_ACCOUNT_ID"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.workersai.chat.options.model", "@cf/meta/llama-2-7b-chat-int8");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.workersai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}

