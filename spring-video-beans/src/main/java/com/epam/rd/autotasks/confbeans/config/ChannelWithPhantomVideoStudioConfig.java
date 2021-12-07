package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.MyChannel;
import com.epam.rd.autotasks.confbeans.video.Video;
import com.epam.rd.autotasks.confbeans.video.VideoStudioImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

public class ChannelWithPhantomVideoStudioConfig {
    private final VideoStudioImpl videoStudio = new VideoStudioImpl();

    @Bean
    public Channel createChannel(Video video1, Video video2, Video video3, Video video4,
                                 Video video5, Video video6, Video video7, Video video8) {
        List<Video> videos = Arrays.asList(video1, video2, video3, video4, video5, video6, video7, video8);
        return new MyChannel(videos);
    }

    @Bean
    @Scope("prototype")
    public Video createVideoFranchise() {
        return videoStudio.produce();
    }
}
