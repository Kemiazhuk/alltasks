package com.epam.rd.autocode.startegy.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoolCard extends AbstractCardDealingStrategy {
    private static final String TRUMP_CARD = "Trump card";
    private static final String REMAINING = "Remaining";
    private static final int QUANTITY_CARD_FOR_PLAYERS = 6;

    @Override
    public Map<String, List<Card>> dealStacks(Deck deck, int players) {
        Map<String, List<Card>> numberCards = dealCardsForPlayers(deck, players, QUANTITY_CARD_FOR_PLAYERS);
        numberCards.put(TRUMP_CARD, new ArrayList<>());
        numberCards.get(TRUMP_CARD).add(deck.dealCard());
        numberCards.put(REMAINING, deck.restCards());
        return numberCards;
    }
}
