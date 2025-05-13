package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

/**
 * Represents the “Princess” card (Princess Annette) in the game.
 * <p>
 * If a player discards this card for any reason, they are immediately eliminated from the round and any further
 * effects of the card that caused the discard do not occur.
 * </p>
 */
public class PrincessCard extends Card {
    final String name = "Princess Annette";
    final int value = 8;
    final String description = "Hampered only by the naïveté of youth, Princess Annette is elegant, charming, and " +
            "beautiful. Obviously, you want the princess to carry your letter. However, she is self-conscious about " +
            "matters of the heart, and if confronted, will toss your letter in the fire and deny looking at any " +
            "correspondence.";
    final String function = "If you discard the Princess—no matter how or why—she has tossed your letter into the " +
            "fire. You are immediately knocked out of the round. If the Princess was discarded by a card effect, any " +
            "remaining effects of that card do not apply (you do not draw a card from the Prince, for example). " +
            "Effects tied to being knocked out the round still apply (eg. Constable, Jester), however.";
    final int amount = 1;
    GameSession gameSession;

    /**
     * Constructs a PrincessCard with the given game session.
     *
     * @param gameSession the current game session context
     */
    public PrincessCard(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    /**
     * {@inheritDoc}
     *
     * @return the name of the card ("Princess Annette")
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the card’s numerical value.
     *
     * @return the value of the card (8)
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the card’s flavor description.
     *
     * @return the description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets detailed information about the card’s effect.
     *
     * @return the function text
     */
    public String getFunction() {
        return function;
    }

    /**
     * Gets the number of copies of this card in the deck.
     *
     * @return the amount (1)
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Executes the effect of the Princess card:
     * the player who discards this card is eliminated from the round.
     * <p>
     * Marks the current player as eliminated and logs the elimination to the console.
     * </p>
     */
    public void play() {
        var currentPlayer = gameSession.getCurrentPlayer();
        if (currentPlayer != null) {
            currentPlayer.setEliminated(true);
            System.out.println("[Princess Effect] " + currentPlayer.getName() + " discarded the Princess and is eliminated!");
        } else {
            System.out.println("[Princess Effect] Unknown player discarded the Princess.");
        }
    }

    /**
     * This card does not support targeting a specific player.
     * @param target the index of the target player
     * @throws UnsupportedOperationException always thrown to indicate no target is allowed
     */
    public void play(int target) {
        throw new UnsupportedOperationException("Princess does not target any player.");
    }

    /**
     * This card does not support targeting a specific player by name.
     * @param targetName the name of the target player
     * @return never returns normally
     * @throws UnsupportedOperationException always thrown to indicate no target is allowed
     */
    public String play(String targetName) {
        throw new UnsupportedOperationException("Princess does not target any player.");
    }
}
