package de.lmu.gamepackage;

import java.util.ArrayList;
import java.util.List;
import de.lmu.gamepackage.gamecards.*;

public class DiscardPile {
    private List<Card> cards;
    private GameSession gameSession;

    public DiscardPile(GameSession gameSession) {
        this.cards = new ArrayList<>();
        this.gameSession = gameSession;
    }

    // Add a card to the discard pile
    public void add(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    // Get the last discarded card (top of stack)
    public Card getLastCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.getLast();
    }

    // Return all cards to the discard pile
    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    // Print discard pile (for debugging)
    public void printDiscardPile() {
        System.out.println("Discard pile:");
        for (Card card : cards) {
            System.out.println("- " + card.getName());
        }
    }
}
