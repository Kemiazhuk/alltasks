package com.epam.rd.autocode.startegy.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TexasHoldem extends AbstractCardDealingStrategy {
    private static final String REMAINING = "Remaining";
    private static final String COMMUNITY = "Community";
    private static final int QUANTITY_CARD_FOR_PLAYERS = 2;
    private static final int QUANTITY_COMMUNITY_CARDS = 5;

    @Override
    public Map<String, List<Card>> dealStacks(Deck deck, int players) {
        Map<String, List<Card>> numberCards = dealCardsForPlayers(deck, players, QUANTITY_CARD_FOR_PLAYERS);
        numberCards.put(COMMUNITY, new ArrayList<>());
        for (int j = 0; j < QUANTITY_COMMUNITY_CARDS; j++) {
            numberCards.get(COMMUNITY).add(deck.dealCard());
        }
        numberCards.put(REMAINING, deck.restCards());
        return numberCards;
    }
}
