package com.rd.epam.autotasks.scopes.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ThreadScopeConfig implements Scope {
    private Map<String, Object> scopedObjects
            = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Object> scopedObjectsWithThreadsName
            = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (scopedObjects.containsKey(name)) {
            if (!scopedObjectsWithThreadsName.containsKey(Thread.currentThread().getName())) {
                scopedObjectsWithThreadsName.put(Thread.currentThread().getName(), objectFactory.getObject());
                scopedObjects.put(name, objectFactory.getObject());
            }
        } else {
            scopedObjectsWithThreadsName.put(Thread.currentThread().getName(), objectFactory.getObject());
            scopedObjects.put(name, objectFactory.getObject());
        }

        return scopedObjectsWithThreadsName.get(Thread.currentThread().getName());
    }

    @Override
    public Object remove(String s) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return "thread";
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new CustomBeanFactoryThread();
    }
}
