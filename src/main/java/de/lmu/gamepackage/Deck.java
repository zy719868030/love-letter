package de.lmu.gamepackage;

import de.lmu.gamepackage.gamecards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards;
    private GameSession gameSession;

    public Deck(GameSession gameSession) {
        this.cards = new Stack<>();
        this.gameSession = gameSession;
        initStandardDeck(gameSession.getPlayers().size()); // Initialize and shuffle

    }

    // Initialize the standard Love Letter stack
    public void initStandardDeck(int playerCount) {

        // Add various types of cards
        for (int i = 0; i < 5; i++) cards.push(new GuardCard(gameSession));
        for (int i = 0; i < 2; i++) cards.push(new PriestCard(gameSession));
        for (int i = 0; i < 2; i++) cards.push(new BaronCard(gameSession));
        for (int i = 0; i < 2; i++) cards.push(new HandmaidCard(gameSession));
        for (int i = 0; i < 2; i++) cards.push(new PrinceCard(gameSession));
        cards.push(new KingCard(gameSession));
        cards.push(new CountessCard(gameSession));
        cards.push(new PrincessCard(gameSession));

        shuffle();

        gameSession.setCardAside(cards.pop());
        //cards.remove(cards.peek());

        if(playerCount == 2){
            List<Card> cardsAside = new ArrayList<>();
            for(int i = 0; i < 3; i++) {
                cardsAside.add(draw());
            }
            gameSession.setCardsAside(cardsAside);
        }
    }

    // shuffle cards
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Draw a card.
    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.pop();
        }
        return null;
    }

    // Displays the current number of remaining cards
    public int remainingCards() {
        return cards.size();
    }

    // Detect if the card stack is empty
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    //View the top card of the stack
    public Card peek() {
        return cards.isEmpty() ? null : cards.peek();
    }
}
