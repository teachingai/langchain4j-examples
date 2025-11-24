package com.github.hiwepy.huggingface.config;

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

    public String getHuggingFaceAccessToken() {
        return getProperty("langchain4j.huggingface.access-token", System.getenv("HF_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.huggingface.chat.options.model", "microsoft/DialoGPT-medium");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.huggingface.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

