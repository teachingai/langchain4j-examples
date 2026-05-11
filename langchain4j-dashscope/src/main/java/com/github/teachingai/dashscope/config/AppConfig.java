package io.github.partmeai.dashscope.config;

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

    public String getDashScopeApiKey() {
        return getProperty("langchain4j.dashscope.api-key", System.getenv("DASHSCOPE_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.dashscope.chat.options.model", "qwen-plus");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.dashscope.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

