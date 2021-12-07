package com.epam.rd.autotasks.confbeans.video;

import java.util.List;

public class MyChannel extends Channel {

    public MyChannel(List<Video> videos) {
        for (Video vid : videos) {
            this.addVideo(vid);
        }
    }

    public MyChannel(VideoStudio videoStudio) {
        for (int i = 0; i < 8; i++) {
            this.addVideo(videoStudio.produce());
        }
    }

}
