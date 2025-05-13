package de.lmu.gamepackage;

import de.lmu.gamepackage.gamecards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents the draw deck in a Love Letter game.
 * <p>
 * Initializes a standard deck of cards based on the number of players, handles shuffling, drawing cards, and
 * tracking the card set‐aside and remaining cards in the deck.
 * </p>
 */
public class Deck {
    private Stack<Card> cards;
    private GameSession gameSession;

    /**
     * Constructs a new deck for the given game session, initializes it with the standard Love Letter card
     * distribution, shuffles it, and removes the initial set‐aside card(s).
     * @param gameSession the game session this deck belongs to
     */
    public Deck(GameSession gameSession) {
        this.cards = new Stack<>();
        this.gameSession = gameSession;
        initStandardDeck(gameSession.getPlayers().size()); // Initialize and shuffle
    }

    /**
     * Returns the number of cards still remaining in the deck.
     * @return the count of cards left to draw
     */
    public int remainDeckCards(){
        return cards.size();
    }


    // Initialize the standard Love Letter stack
    /**
     * Initializes the deck with the standard Love Letter distribution:
     * <ul>
     *   <li>5 Guards</li>
     *   <li>2 Priests</li>
     *   <li>2 Barons</li>
     *   <li>2 Handmaids</li>
     *   <li>2 Princes</li>
     *   <li>1 King</li>
     *   <li>1 Countess</li>
     *   <li>1 Princess</li>
     * </ul>
     * Then shuffles, removes one card to set aside, and if there are only two players, removes three additional
     * cards to a separate aside pile.
     * @param playerCount the number of players in the game
     */
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


    /**
     * Randomly shuffles the order of the cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws (removes and returns) the top card of the deck.
     * @return the drawn card, or null if the deck is empty
     */
    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.pop();
        }
        return null;
    }

    /**
     * Returns the number of cards still remaining in the deck.
     * (Alias for {@link #remainDeckCards()}.)
     * @return remaining card count
     */
    public int remainingCards() {
        return cards.size();
    }

    /**
     * Checks whether the deck is empty.
     * @return true if no cards remain, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    //View the top card of the stack
    /**
     * Peeks at the top card of the deck without removing it.
     * @return the topmost card, or null if the deck is empty
     */
    public Card peek() {
        return cards.isEmpty() ? null : cards.peek();
    }
}
