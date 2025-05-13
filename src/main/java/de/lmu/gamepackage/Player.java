package de.lmu.gamepackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a Love Letter game, tracking their hand, elimination status, and the sum of card values they
 * have discarded this round.
 */
public class Player {
    private String name;
    private List<Card> hand;
    private boolean isOut;
    private boolean eliminated = false;
    private int discardedCards = 0;

    /**
     * Constructs a new Player with the given name.
     * @param name the unique nickname of this player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isOut = false;
    }

    /**
     * Add a card to your hand
     * @param card the Card to add; ignored if null
     */
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    /**
     * Plays (removes) the card at the specified index from the hand
     * @param cardNumber index of the card to play (0-based)
     * @return the removed Card, or null if index is out of range
     */
    public Card playCard(int cardNumber) {
        if (cardNumber < 0 || cardNumber >= hand.size()) return null;
        return hand.remove(cardNumber);
    }

    /**
     * only play Countess card, another overload of the playCard function
     * @return the Countess Card if present, otherwise null
     */
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

    /**
     * Increases the total of discarded card values by the given amount.
     * @param number the value to add to discardedCards
     * @return the new total discardedCards
     */
    public int increaseDiscardedCardsNumber(int number) {
        this.discardedCards += number;
        return this.discardedCards;
    }

    /**
     * Returns the sum of values of cards this player has discarded this round.
     * @return the discarded cards total value
     */
    public int getDiscardedCards(){
        return this.discardedCards;
    }

    /**
     * Resets the discarded cards total to zero.
     * Should be called at the start of each new round.
     */
    public void resetDiscardedCardsNumber() {
        this.discardedCards = 0;
    }

    /**
     * Clears this player's hand.
     * Used when resetting round state.
     */
    public void resetHand(){
        this.hand.clear();
    }

    /**
     * Clears the "isOut" flag.
     * Used when resetting round state.
     */
    public void resetisOut(){
        this.isOut = false;
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

    // Get current hand (read-only copy)
    /**
     * Returns an unmodifiable view of the player's current hand.
     * @return list of Cards in hand
     */
    public List<Card> getHand() {
        return this.hand;
    }

    /**
     * Formats the player's current hand as a readable string.
     * @return a String like "Your hand: [value:Name] [...]"
     */
    public String handToString() {
        StringBuilder result = new StringBuilder("Your hand: ");
        for (Card card : hand) {
            result.append("["+card.getValue()+":");
            result.append(card.getName());
            result.append("] ");
        }
        return result.toString();
    }

    /**
     * Returns the Countess in hand, if any, without removing it.
     * @return the Countess Card or null if not held
     */
    public Card returnCountess(){
        for (Card card : hand) {
            if(card.getName().equals("Countess")){
                return card;
            }
        }
        return null;
    }

    // Add a convenience method for getting another card in your hand (for card comparisons)
    /**
     * Retrieves the first card in hand.
     * @return the Card at index 0, or null if the hand is empty
     */
    public Card getFirstCard() {
        return hand.isEmpty() ? null : hand.get(0);
    }

    /**
     * @return this player's nickname
     */
    public String getName() {
        return name;
    }

    /**
     * @return true if the player has been marked out for the round
     */
    public boolean isOut() {
        return isOut;
    }

    /**
     * Sets whether this player is out for the current round.
     * @param out true to mark out, false otherwise
     */
    public void setOut(boolean out) {
        isOut = out;
    }

    /**
     * @return true if the player has been eliminated from the game
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Marks this player as eliminated or not.
     * @param eliminated true to eliminate, false to keep playing
     */
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    /**
     * Replaces this player's hand with a new list.
     * @param hand the new hand list
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * Adds a card to this player's hand. Alias for {@link #addCard(Card)}.
     * @param card the Card to draw
     */
    public void drawCard(Card card) {
        this.hand.add(card);
    }

    // HandmaidCard
    private boolean protectedByHandmaid = false;

    /**
     * @return true if the player is currently protected by Handmaid
     */
    public boolean isProtected() {
        return protectedByHandmaid;
    }

    /**
     * Sets or clears Handmaid protection for this player.
     * @param value true to grant protection, false to remove
     */
    public void setProtected(boolean value) {
        this.protectedByHandmaid = value;
    }

    // Debugging
    /**
     * Prints this player's hand to standard output (for debugging).
     */
    public void printHand() {
        System.out.print(name + "'s hand: ");
        for (Card card : hand) {
            System.out.print(card.getName() + " ");
        }
        System.out.println();
    }
}
