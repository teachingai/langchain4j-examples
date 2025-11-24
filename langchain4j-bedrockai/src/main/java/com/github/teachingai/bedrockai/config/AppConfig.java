package com.github.teachingai.bedrockai.config;

import software.amazon.awssdk.regions.Region;

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

    public Region getRegion() {
        String regionStr = getProperty("langchain4j.bedrock.region", "us-east-1");
        return Region.of(regionStr);
    }

    public String getChatModel() {
        return getProperty("langchain4j.bedrock.chat.options.model", "anthropic.claude-3-sonnet-20240229-v1:0");
    }

    public Float getTemperature() {
        String temp = getProperty("langchain4j.bedrock.chat.options.temperature");
        return temp != null ? Float.parseFloat(temp) : 0.7f;
    }

    public Integer getMaxTokens() {
        String maxTokens = getProperty("langchain4j.bedrock.chat.options.max-tokens");
        return maxTokens != null ? Integer.parseInt(maxTokens) : 300;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

