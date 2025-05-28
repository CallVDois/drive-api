package com.callv2.drive.infrastructure.configuration.properties.aspect;

import java.util.List;

public class PointcutAdvisorProperties {

    private String expression;

    private List<String> before;
    private List<String> after;
    private List<String> error;

    public PointcutAdvisorProperties() {
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<String> getBefore() {
        return before;
    }

    public void setBefore(List<String> before) {
        this.before = before;
    }

    public List<String> getAfter() {
        return after;
    }

    public void setAfter(List<String> after) {
        this.after = after;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

}
