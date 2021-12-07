package com.epam.rd.autocode.startegy.cards;

import java.util.List;
import java.util.Map;

public class BridgeCard extends AbstractCardDealingStrategy {

    @Override
    public Map<String, List<Card>> dealStacks(Deck deck, int players) {
        return dealCardsForPlayers(deck, players, deck.size() / players);
    }
}
