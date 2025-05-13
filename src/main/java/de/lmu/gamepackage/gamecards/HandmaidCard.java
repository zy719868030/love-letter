package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

/**
 * Represents the “Handmaid” card (Handmaid Susannah) in the game.
 * <p>
 * When this card is discarded, the current player becomes immune to other players’ card effects
 * until the start of their next turn. If all other players are protected by the Handmaid,
 * any effect that requires choosing another player must instead target the player themself.
 * </p>
 */
public class HandmaidCard extends Card {
    final String name = "Handmaid Susannah";
    final int value = 4;
    final String description = "Few would trust a mere Handmaid with a letter of importance. Fewer still understand " +
            "Susannah’s cleverness, or her skilled ability at playing the foolish Handmaid. That the Queen’s " +
            "confidante and loyal servant escaped any attention after the Queen’s arrest is a testament to her " +
            "clever mind.";
    final String function = "When you discard the Handmaid, you are immune to the effects of other players’ cards " +
            "until the start of your next turn. If all players other than the player whose turn it is are protected " +
            "by the Handmaid, the player must choose him or herself for a card’s effects, if possible.";
    final int amount = 2;
    GameSession gameSession;

    /**
     * Constructs a HandmaidCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public HandmaidCard(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getFunction() {
        return function;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * Handmaid does not support targeting by player index.
     *
     * @param target unused
     */
    public void play(int target) {
    }

    /**
     * Handmaid does not support targeted play by player name.
     *
     * @param targetName unused
     * @throws UnsupportedOperationException always thrown to indicate no target is allowed
     */
    public String play(String targetName) {
        throw new UnsupportedOperationException("Handmaid does not support targeted play.");
    }

    /**
     * Executes the Handmaid effect: protects the current player from other players' effects
     * until the start of their next turn.
     * <p>
     * Marks the current player as protected and logs the action. Also counts this card
     * toward the player's discarded cards total.
     * </p>
     */
    public void play() {
        Player currentPlayer = gameSession.getCurrentPlayer();
        currentPlayer.increaseDiscardedCardsNumber(4);
        if (currentPlayer != null) {
            currentPlayer.setProtected(true);
            System.out.println("[INFO] " + currentPlayer.getName() + " is now protected by Handmaid.");
        }
    }
}
