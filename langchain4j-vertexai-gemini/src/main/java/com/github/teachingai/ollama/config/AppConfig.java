package com.github.teachingai.ollama.config;

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

    public String getProject() {
        return getProperty("langchain4j.vertex-ai-gemini.project", System.getenv("GOOGLE_CLOUD_PROJECT"));
    }

    public String getLocation() {
        return getProperty("langchain4j.vertex-ai-gemini.location", "us-central1");
    }

    public String getChatModel() {
        return getProperty("langchain4j.vertex-ai-gemini.chat.options.model", "gemini-pro");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.vertex-ai-gemini.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }
}

