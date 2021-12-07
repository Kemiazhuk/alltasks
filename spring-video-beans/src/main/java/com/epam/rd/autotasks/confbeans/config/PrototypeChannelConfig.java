package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.MyChannel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

import static com.epam.rd.autotasks.confbeans.config.SingletonChannelWithPrototypeVideosConfig.NAME_FIRST_VIDEO;
import static com.epam.rd.autotasks.confbeans.config.SingletonChannelWithPrototypeVideosConfig.NAME_SECOND_VIDEO;
import static com.epam.rd.autotasks.confbeans.config.SingletonChannelWithPrototypeVideosConfig.NAME_THIRD_VIDEO;

public class PrototypeChannelConfig {

    @Bean
    public Video video1() {
        return new Video(NAME_FIRST_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 10));
    }

    @Bean
    public Video video2() {
        return new Video(NAME_SECOND_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 11));
    }

    @Bean
    public Video video3() {
        return new Video(NAME_THIRD_VIDEO, LocalDateTime.of(2020, 10, 10, 10, 12));
    }

    @Bean
    @Scope("prototype")
    public Channel createChannel(List<Video> videoList) {
        return new MyChannel(videoList);
    }
}
