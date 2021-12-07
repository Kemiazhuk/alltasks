package com.epam.rd.autotasks.springstatefulcalc;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExpressionData {
    private String expression;
    private Map<String, String> allValues;

    public ExpressionData() {
        this.expression = "";
        this.allValues = new HashMap<>();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Integer> getAllValues() {
        return allValues.entrySet()
                .stream()
                .collect(Collectors
                        .toMap(Map.Entry::getKey, e -> Integer.valueOf(e.getValue())));
    }

    public boolean setValue(String key, String value) {
        while (allValues.containsKey(value)) {
            value = allValues.get(value);
        }
        if (allValues.containsKey(key)) {
            allValues.replace(key, value);
            return false;
        } else
            allValues.put(key, value);
        return true;
    }

    public void deleteValue (String key){
        allValues.remove(key);
    }
}
