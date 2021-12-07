package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.MyChannel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

public class SingletonChannelWithPrototypeVideosConfig {
    public final static String NAME_FIRST_VIDEO = "How to boil water";
    public final static String NAME_SECOND_VIDEO = "How to build a house";
    public final static String NAME_THIRD_VIDEO = "How to escape solitude";

    @Bean
    @Scope("prototype")
    public Video video1() {
        return new Video(NAME_FIRST_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 10));
    }

    @Bean
    @Scope("prototype")
    public Video video2() {
        return new Video(NAME_SECOND_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 11));
    }

    @Bean
    @Scope("prototype")
    public Video video3() {
        return new Video(NAME_THIRD_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 12));
    }

    @Bean
    public Channel createChannel(List<Video> videoList) {
        return new MyChannel(videoList);
    }
}
