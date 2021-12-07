package com.epam.rd.autocode.factory.plot;

public class ClassicDisneyPlotFactory implements PlotFactory {
    Character hero;
    Character beloved;
    Character villain;

    public ClassicDisneyPlotFactory(Character hero, Character beloved, Character villain) {
        this.hero = hero;
        this.beloved = beloved;
        this.villain = villain;
    }

    @Override
    public Plot plot() {
        return new DisneyPlot(hero, beloved, villain);
    }
}
