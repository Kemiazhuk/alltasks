package com.epam.rd.autocode.startegy.cards;

import java.util.List;
import java.util.Map;

public class PokerCard extends AbstractCardDealingStrategy {
    private static final String REMAINING = "Remaining";
    private static final int QUANTITY_CARD_FOR_PLAYERS = 5;

    @Override
    public Map<String, List<Card>> dealStacks(Deck deck, int players) {
        Map<String, List<Card>> numberCards = dealCardsForPlayers(deck, players, QUANTITY_CARD_FOR_PLAYERS);
        numberCards.put(REMAINING, deck.restCards());
        return numberCards;
    }


}
