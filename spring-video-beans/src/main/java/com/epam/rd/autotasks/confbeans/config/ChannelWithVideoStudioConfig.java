package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.MyChannel;
import com.epam.rd.autotasks.confbeans.video.VideoStudio;
import com.epam.rd.autotasks.confbeans.video.VideoStudioImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelWithVideoStudioConfig {

    @Bean
    public Channel createChannel(VideoStudio videoStudio) {
        return new MyChannel(videoStudio);
    }

    @Bean
    public VideoStudio createVideoStudio() {
        return new VideoStudioImpl();
    }
}
