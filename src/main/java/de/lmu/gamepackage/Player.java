package de.lmu.gamepackage;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isOut;
    private int score = 0;
    private boolean eliminated = false;
    private int discardedCards = 0;

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
        if (cardNumber < 0 || cardNumber >= hand.size()) return null;
        return hand.remove(cardNumber);
    }

    //only play countness card, another overload of the playCard function
    public Card playCard() {
        int cardNumber;
        for(Card card : hand) {
            if (card.getName().equals("Countess Wilhelmina")) {
                hand.remove(card);
                return card;
            }
        }
        return null;
    }

    //public Card playCard(int cardNumber) {
        //Card playedCard = null;
        //cardNumber only could be 0 or 1. 0 for old card, 1 for the new card.
        //if (cardNumber == 0) {
           //playedCard = this.hand.getFirst();
            //this.hand.removeFirst();
        //}
        //else if (cardNumber == 1) {
        //    playedCard = this.hand.get(1);
        //    this.hand.remove(1);
        //}
       //return playedCard;
    //}

    // 获取当前手牌（只读副本）
    public List<Card> getHand() {
        return this.hand;
    }
    public String handToString() {
        StringBuilder result = new StringBuilder("Your hand: ");
        for (Card card : hand) {
            result.append(card.getName());
            result.append(" ");
        }
        return result.toString();
    }
    public Card returnCountess(){
        for (Card card : hand) {
            if(card.getName().equals("Countess")){
                return card;
            }
        }
        return null;
    }

    //添加一个用于获取手牌中另一张的便捷方法（用于卡牌比较）
    public Card getFirstCard() {
        return hand.isEmpty() ? null : hand.get(0);
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

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }
    public void drawCard(Card card) {
        this.hand.add(card);
    }

    // HandmaidCard
    private boolean protectedByHandmaid = false;

    public boolean isProtected() {
        return protectedByHandmaid;
    }

    public void setProtected(boolean value) {
        this.protectedByHandmaid = value;
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
