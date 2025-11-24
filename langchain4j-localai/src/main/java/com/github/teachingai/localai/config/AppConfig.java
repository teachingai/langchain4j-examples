package com.github.teachingai.localai.config;

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

    public String getLocalAiBaseUrl() {
        return getProperty("langchain4j.local-ai.base-url", "http://localhost:8080");
    }

    public String getChatModel() {
        return getProperty("langchain4j.local-ai.chat.options.model", "gpt-3.5-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.local-ai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

