package io.github.partmeai.config;

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

    public String getMinimaxApiKey() {
        return getProperty("langchain4j.minimax.api-key", System.getenv("MINIMAX_API_KEY"));
    }

    public String getMinimaxGroupId() {
        return getProperty("langchain4j.minimax.group-id", System.getenv("MINIMAX_GROUP_ID"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.minimax.chat.options.model", "abab5.5-chat");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.minimax.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}

