package com.epam.rd.autocode.startegy.cards;

public class CardDealingStrategies {
    public static CardDealingStrategy texasHoldemCardDealingStrategy() {
        return new TexasHoldem();
    }

    public static CardDealingStrategy classicPokerCardDealingStrategy() {
        return new PokerCard();
    }

    public static CardDealingStrategy bridgeCardDealingStrategy() {
        return new BridgeCard();
    }

    public static CardDealingStrategy foolCardDealingStrategy() {
        return new FoolCard();
    }
}
