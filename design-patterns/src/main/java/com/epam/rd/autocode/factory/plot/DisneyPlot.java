package com.epam.rd.autocode.factory.plot;

import java.text.MessageFormat;

public class DisneyPlot implements Plot {
    Character hero;
    Character beloved;
    Character villain;

    public DisneyPlot(Character hero, Character beloved, Character villain) {
        this.hero = hero;
        this.beloved = beloved;
        this.villain = villain;
    }

    @Override
    public String toString() {

        return MessageFormat.format("{0} is great. {0} and {1} love each other. {2} interferes with their happiness but loses in the end.", hero.name(), beloved.name(), villain.name());
    }
}
