package com.epam.rd.autotasks.confbeans.video;

import java.time.LocalDateTime;

public class VideoStudioImpl implements VideoStudio {
    private int YEAR = 1999;
    private int NUMBER_CHAPTER = 0;
    private final static String NAME_SHOW = "Cat & Curious ";

    @Override
    public Video produce() {
        YEAR += 2;
        NUMBER_CHAPTER++;
        return new Video(NAME_SHOW + NUMBER_CHAPTER, LocalDateTime.of(YEAR, 10, 18, 10, 00));
    }
}
