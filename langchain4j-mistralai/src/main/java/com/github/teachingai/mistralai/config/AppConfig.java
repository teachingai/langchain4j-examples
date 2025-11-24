package com.github.teachingai.mistralai.config;

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

    public String getMistralAiApiKey() {
        return getProperty("langchain4j.mistralai.api-key", System.getenv("MISTRAL_AI_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.mistralai.chat.options.model", "mistral-small");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.mistralai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}


