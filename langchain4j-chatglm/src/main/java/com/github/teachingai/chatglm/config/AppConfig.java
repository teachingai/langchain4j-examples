package com.github.teachingai.chatglm.config;

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

    public String getChatGlmApiKey() {
        return getProperty("langchain4j.chatglm.api-key", System.getenv("CHATGLM_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.chatglm.chat.options.model", "glm-4");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.chatglm.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

