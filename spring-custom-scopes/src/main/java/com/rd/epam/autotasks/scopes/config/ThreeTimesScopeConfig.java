package com.rd.epam.autotasks.scopes.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreeTimesScopeConfig implements Scope {
    private Map<String, CountingBean> scopedObjects
            = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Runnable> destructionCallbacks
            = Collections.synchronizedMap(new HashMap<>());
    private static final int THREE_TIMES_ACCESS = 3;
    private static final int DROP_COUNTER = 0;

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        CountingBean myBean = scopedObjects.get(name);
        if (myBean == null) {
            myBean = new CountingBean(objectFactory.getObject());
            scopedObjects.put(name, myBean);
        } else if (myBean.compareCounter()) {
            myBean.setMyBean(objectFactory.getObject());
        }
        myBean.incrementCounterAccessing();
        return scopedObjects.get(name).getMyBean();
    }

    @Override
    public Object remove(String name) {
        destructionCallbacks.remove(name);
        return scopedObjects.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCallbacks.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String s) {
        return "threeTimes";
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new CustomBeanFactoryTreeTimes();
    }

    private class CountingBean {
        private final AtomicInteger counterAccessing = new AtomicInteger(0);
        private Object myBean;

        public CountingBean(Object myBean) {
            this.myBean = myBean;
        }

        public AtomicInteger getCounterAccessing() {
            return counterAccessing;
        }

        public void incrementCounterAccessing() {
            counterAccessing.incrementAndGet();
        }

        public boolean compareCounter() {
            return counterAccessing.compareAndSet(THREE_TIMES_ACCESS, DROP_COUNTER);
        }

        public Object getMyBean() {
            return myBean;
        }

        public void setMyBean(Object myBean) {
            this.myBean = myBean;
        }
    }
}
