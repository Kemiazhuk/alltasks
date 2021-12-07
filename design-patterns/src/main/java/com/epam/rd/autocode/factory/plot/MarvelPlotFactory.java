package com.epam.rd.autocode.factory.plot;

public class MarvelPlotFactory implements PlotFactory {
    Character[] heroes;
    EpicCrisis epicCrisis;
    Character villain;

    public MarvelPlotFactory(Character[] heroes, EpicCrisis epicCrisis, Character villain) {
        this.heroes = heroes;
        this.epicCrisis = epicCrisis;
        this.villain = villain;
    }

    @Override
    public Plot plot() {
        return new Marvel(heroes, epicCrisis, villain);
    }
}
