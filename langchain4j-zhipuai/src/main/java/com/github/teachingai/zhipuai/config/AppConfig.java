package io.github.partmeai.zhipuai.config;

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

    public String getZhipuAiApiKey() {
        return getProperty("langchain4j.zhipuai.api-key", System.getenv("ZHIPUAI_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.zhipuai.chat.options.model", "glm-3-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.zhipuai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}

