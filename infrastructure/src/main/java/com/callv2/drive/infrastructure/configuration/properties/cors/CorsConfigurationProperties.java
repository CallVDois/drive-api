package com.callv2.drive.infrastructure.configuration.properties.cors;

import java.util.List;

public class CorsConfigurationProperties {

    private String pattern;
    private List<String> allowedOriginsPatterns;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private boolean allowCredentials;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<String> getAllowedOriginsPatterns() {
        return allowedOriginsPatterns;
    }

    public void setAllowedOriginsPatterns(List<String> allowedOriginsPatterns) {
        this.allowedOriginsPatterns = allowedOriginsPatterns;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

}
