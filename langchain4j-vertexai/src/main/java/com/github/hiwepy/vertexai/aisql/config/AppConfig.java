package com.github.hiwepy.vertexai.aisql.config;

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

    public String getEndpoint() {
        return getProperty("langchain4j.vertex-ai.endpoint", "us-central1-aiplatform.googleapis.com:443");
    }

    public String getProject() {
        return getProperty("langchain4j.vertex-ai.project", System.getenv("GOOGLE_CLOUD_PROJECT"));
    }

    public String getLocation() {
        return getProperty("langchain4j.vertex-ai.location", "us-central1");
    }

    public String getPublisher() {
        return getProperty("langchain4j.vertex-ai.publisher", "google");
    }

    public String getChatModel() {
        return getProperty("langchain4j.vertex-ai.chat.options.model", "chat-bison@001");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.vertex-ai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public String getJdbcUrl() {
        return getProperty("spring.datasource.url", "jdbc:h2:mem:testdb");
    }

    public String getJdbcUsername() {
        return getProperty("spring.datasource.username", "sa");
    }

    public String getJdbcPassword() {
        return getProperty("spring.datasource.password", "");
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

