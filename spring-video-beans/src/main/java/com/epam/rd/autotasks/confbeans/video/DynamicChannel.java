package com.epam.rd.autotasks.confbeans.video;

import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;

public class DynamicChannel extends Channel {
    private ApplicationContext context;

    public DynamicChannel(ApplicationContext context) {
        this.context = context;
    }

    public Stream<Video> videos() {
        return Stream.of(context.getBean(Video.class), context.getBean(Video.class), context.getBean(Video.class),
                context.getBean(Video.class), context.getBean(Video.class), context.getBean(Video.class),
                context.getBean(Video.class));
    }
}
