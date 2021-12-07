package com.epam.rd.autocode.factory.plot;

import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Marvel implements Plot {
    Character[] heroes;
    EpicCrisis epicCrisis;
    Character villain;

    public Marvel(Character[] heroes, EpicCrisis epicCrisis, Character villain) {
        this.heroes = heroes;
        this.epicCrisis = epicCrisis;
        this.villain = villain;
    }

    @Override
    public String toString() {
        StringBuilder stringHeroes = new StringBuilder();
        for (int i = 0; i < heroes.length; i++) {
            if (i == 0) {
                stringHeroes.append(" brave ").append(heroes[i].name());
            } else {
                stringHeroes.append(", brave ").append(heroes[i].name());
            }
        }
        return MessageFormat.format("{1} threatens the world. But{0} on guard. So, no way that intrigues of {2} overcome the willpower of inflexible heroes", stringHeroes.toString(), epicCrisis.name(), villain.name());
    }
}
