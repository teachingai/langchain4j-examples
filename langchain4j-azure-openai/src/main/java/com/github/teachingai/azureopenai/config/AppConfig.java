package io.github.partmeai.azureopenai.config;

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

    public String getAzureOpenAiApiKey() {
        return getProperty("langchain4j.azure.openai.api-key", System.getenv("AZURE_OPENAI_API_KEY"));
    }

    public String getAzureOpenAiEndpoint() {
        return getProperty("langchain4j.azure.openai.endpoint", System.getenv("AZURE_OPENAI_ENDPOINT"));
    }

    public String getChatModel() {
        return getProperty("langchain4j.azure.openai.chat.options.model", "gpt-35-turbo");
    }

    public Double getTemperature() {
        String temp = getProperty("langchain4j.azure.openai.chat.options.temperature");
        return temp != null ? Double.parseDouble(temp) : 0.7;
    }
}


