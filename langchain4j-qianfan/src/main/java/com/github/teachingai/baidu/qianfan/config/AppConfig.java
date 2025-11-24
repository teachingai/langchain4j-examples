package com.github.teachingai.baidu.qianfan.config;

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

    public String getQianfanApiKey() {
        return getProperty("langchain4j.qianfan.api-key", System.getenv("QIANFAN_API_KEY"));
    }

    public String getQianfanSecretKey() {
        return getProperty("langchain4j.qianfan.secret-key", System.getenv("QIANFAN_SECRET_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.qianfan.chat.options.model", "ERNIE-Bot-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.qianfan.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

