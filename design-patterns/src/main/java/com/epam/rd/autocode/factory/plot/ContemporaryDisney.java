package com.epam.rd.autocode.factory.plot;

import java.text.MessageFormat;

public class ContemporaryDisney implements Plot {
    Character hero;
    EpicCrisis epicCrisis;
    Character funnyFriend;

    public ContemporaryDisney(Character hero, EpicCrisis epicCrisis, Character funnyFriend) {
        this.hero = hero;
        this.epicCrisis = epicCrisis;
        this.funnyFriend = funnyFriend;
    }

    @Override
    public String toString() {

        return MessageFormat.format("{0} feels a bit awkward and uncomfortable. But personal issues fades, when a big trouble comes - {1}. {0} stands up against it, but with no success at first.But putting self together and help of friends, including spectacular funny {2} restore the spirit and {0} overcomes the crisis and gains gratitude and respect"
                , hero.name(), epicCrisis.name(), funnyFriend.name());
    }
}
