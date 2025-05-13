package de.lmu.gamepackage;

/**
 * Abstract base class for all Love Letter game cards.
 * <p>
 * Each concrete card type extends this class and implements its play behavior. Holds common properties like name,
 * value, description, and functional text.
 * </p>
 */
public abstract class Card {
    protected String name;
    protected int value;
    protected String description;
    protected String function;
    protected int amount;
    protected GameSession  gameSession;

    /**
     * Default constructor for subclass initialization.
     */
    public Card(){};

    /**
     * Returns the name of the card.
     * @return the card’s name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the numeric value of the card.
     * @return the card’s point value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the descriptive text of the card.
     * @return the card’s description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the functional text explaining how the card works.
     * @return the card’s function text
     */
    public String getFunction() {
        return function;
    }

    /**
     * Returns the total number of this card type in the deck.
     * @return the card’s amount/copies
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Executes the card’s effect when it does not target another player.
     * <p>
     * Subclasses override this for cards like Handmaid that protect the current player without specifying a target.
     * </p>
     */
    public abstract void play();                          // For no-target cards

    /**
     * Executes the card’s effect when it targets by hand‐index.
     * <p>
     * Some cards use a numeric index (e.g. choosing your newly drawn card vs. your old card). Override in subclasses
     * that require index‐based targeting.
     * </p>
     * @param target the index of the target card in the current player’s hand
     */
    public abstract void play(int target);                // For index-based cards

    /**
     * Executes the card’s effect when it targets another player by name.
     * @param targetName the nickname of the player to target
     * @return a result or info string about the effect outcome
     */
    public abstract String play(String targetName);

    /**
     * Executes the card’s effect when it also requires a guessed value.
     * <p>
     * By default, most cards do not support this. Subclasses like GuardCard override this to allow guessing another
     * player’s card.
     * </p>
     * @param targetName       the nickname of the player to target
     * @param guessedCardName  the guessed card value or name as string
     * @return a result or info string about the effect outcome
     */
    public String play(String targetName, String guessedCardName) {
        throw new UnsupportedOperationException("This card does not support guessing play.");
    }
}
