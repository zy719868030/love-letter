package de.lmu.gamepackage;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isOut;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isOut = false;
    }

    // 添加一张卡牌到手牌
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }


    public Card playCard(int cardNumber) {
        Card playedCard = null;
        //cardNumber only could be 0 or 1. 0 for old card, 1 for the new card.
        if (cardNumber == 0) {
            playedCard = this.hand.getFirst();
            this.hand.removeFirst();
        }
        else if (cardNumber == 1) {
            playedCard = this.hand.get(1);
            this.hand.remove(1);
        }
       return playedCard;
    }

    // 获取当前手牌（只读副本）
    public List<Card> getHand() {
        return this.hand;
    }

    public String getName() {
        return name;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    // 调试/展示
    public void printHand() {
        System.out.print(name + "'s hand: ");
        for (Card card : hand) {
            System.out.print(card.getName() + " ");
        }
        System.out.println();
    }
}
