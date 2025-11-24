package com.github.hiwepy.llmsfreeapi.config;

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

    public String getBaseUrl() {
        return getProperty("langchain4j.llmsfreeapi.base-url", "https://api.llmsfreeapi.com");
    }

    public String getApiKey() {
        return getProperty("langchain4j.llmsfreeapi.api-key", System.getenv("LLMSFREEAPI_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.llmsfreeapi.chat.options.model", "gpt-3.5-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.llmsfreeapi.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

