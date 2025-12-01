package com.newsfilter.demofilter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "internal.api")
public class InternalAuthProperties {

    /**
     * Shared secret used by n8n to authorize internal ingestion endpoint.
     */
    private String key = "change-me";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
