package com.github.teachingai.anthropic.config;

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

    public String getAnthropicApiKey() {
        return getProperty("langchain4j.anthropic.api-key", System.getenv("ANTHROPIC_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.anthropic.chat.options.model", "claude-3-5-sonnet-20241022");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.anthropic.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}


