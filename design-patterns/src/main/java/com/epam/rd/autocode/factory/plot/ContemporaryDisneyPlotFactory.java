package com.epam.rd.autocode.factory.plot;

public class ContemporaryDisneyPlotFactory implements PlotFactory {
    Character hero;
    EpicCrisis epicCrisis;
    Character funnyFriend;

    public ContemporaryDisneyPlotFactory(Character hero, EpicCrisis epicCrisis, Character funnyFriend) {
        this.hero = hero;
        this.epicCrisis = epicCrisis;
        this.funnyFriend = funnyFriend;
    }

    @Override
    public Plot plot() {
        return new ContemporaryDisney(hero, epicCrisis, funnyFriend);
    }
}
