package com.github.teachingai.moonshotai.config;

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

    public String getMoonshotAiApiKey() {
        return getProperty("langchain4j.moonshotai.api-key", System.getenv("MOONSHOTAI_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.moonshotai.chat.options.model", "moonshot-v1-8k");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.moonshotai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

