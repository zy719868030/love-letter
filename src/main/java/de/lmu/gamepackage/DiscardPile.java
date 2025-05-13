package de.lmu.gamepackage;

import java.util.ArrayList;
import java.util.List;
import de.lmu.gamepackage.gamecards.*;

/**
 * Represents the pile of discarded cards for the current round.
 * <p>
 * Tracks all cards that have been played/discarded. Provides methods to add new discards, inspect the top of the
 * pile, and retrieve the full discard history.
 * </p>
 */
public class DiscardPile {
    private List<Card> cards;
    private GameSession gameSession;

    /**
     * Creates a new discard pile associated with the given game session.
     * @param gameSession the game session this discard pile belongs to
     */
    public DiscardPile(GameSession gameSession) {
        this.cards = new ArrayList<>();
        this.gameSession = gameSession;
    }

    // Add a card to the discard pile
    /**
     * Adds the specified card to the top of the discard pile.
     * @param card the card to discard; if null, nothing happens
     */
    public void add(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    // Get the last discarded card (top of stack)
    /**
     * Retrieves the most recently discarded card.
     * @return the card at the top of the discard pile, or null if the pile is empty
     */
    public Card getLastCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.getLast();
    }

    // Return all cards to the discard pile
    /**
     * Returns a snapshot of all cards that have been discarded so far.
     * <p>
     * The returned list is a copy; modifying it does not affect the internal pile.
     * </p>
     * @return a new list containing every card in the discard pile, in discard order
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Prints the contents of the discard pile to standard output.
     * <p>
     * Primarily intended for debugging purposes. Outputs each card's name in the order they were discarded.
     * </p>
     */
    public void printDiscardPile() {
        System.out.println("Discard pile:");
        for (Card card : cards) {
            System.out.println("- " + card.getName());
        }
    }
}
