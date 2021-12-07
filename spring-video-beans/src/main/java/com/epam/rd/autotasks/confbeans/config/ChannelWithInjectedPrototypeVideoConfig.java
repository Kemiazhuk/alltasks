package com.epam.rd.autotasks.confbeans.config;

import java.time.LocalDateTime;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.DynamicChannel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class ChannelWithInjectedPrototypeVideoConfig {
    public static final String NAME_SHOW = "Cat Failure Compilation";
    public static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    @Bean
    @Scope("prototype")
    public Video createVideoFranchise() {
        System.out.println(LOCAL_DATE_TIME + " create");
        Video video = new Video(NAME_SHOW, LOCAL_DATE_TIME);
        LOCAL_DATE_TIME = LOCAL_DATE_TIME.plusDays(1);
        return video;
    }

    @Bean
    public Channel createChannel(ApplicationContext context) {
        return new DynamicChannel(context);
    }
}
