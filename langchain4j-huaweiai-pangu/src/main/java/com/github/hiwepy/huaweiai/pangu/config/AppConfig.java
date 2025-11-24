package com.github.hiwepy.huaweiai.pangu.config;

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

    public String getHuaweiAiPanguApiKey() {
        return getProperty("langchain4j.huaweiai-pangu.api-key", System.getenv("HUAWEIAI_PANGU_API_KEY"));
    }

    public String getHuaweiAiPanguSecretKey() {
        return getProperty("langchain4j.huaweiai-pangu.secret-key", System.getenv("HUAWEIAI_PANGU_SECRET_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.huaweiai-pangu.chat.options.model", "pangu-alpha");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.huaweiai-pangu.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

