package com.tianyin.stepfun.config;

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

    public String getStepFunApiKey() {
        return getProperty("langchain4j.stepfun.api-key", System.getenv("STEPFUN_API_KEY"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.stepfun.chat.options.model", "step-1-32k");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.stepfun.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

