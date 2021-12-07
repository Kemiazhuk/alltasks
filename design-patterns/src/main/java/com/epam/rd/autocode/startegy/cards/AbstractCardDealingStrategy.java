package com.epam.rd.autocode.startegy.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCardDealingStrategy implements CardDealingStrategy {
    private static final String PLAYER = "Player ";

    protected Map<String, List<Card>> dealCardsForPlayers(Deck deck, int players, int quantityCardForPlayers) {
        Map<String, List<Card>> numberCards = initializationPlayers(players);
        for (int i = 0; i < quantityCardForPlayers; i++) {
            for (Map.Entry<String, List<Card>> entry : numberCards.entrySet()) {
                entry.getValue().add(deck.dealCard());
            }
        }
        return numberCards;
    }

    protected Map<String, List<Card>> initializationPlayers(int players) {
        Map<String, List<Card>> allPlayers = new TreeMap<>();
        for (int j = players; j > 0; j--) {
            allPlayers.put(PLAYER + j, new ArrayList<>());
        }
        return allPlayers;
    }
}
