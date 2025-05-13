package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

/**
 * Represents the “King” card (King Arnaud IV) in the game.
 * <p>
 * When this card is played (discarded), the current player trades the card in their hand
 * with the card held by another chosen player who is still in the round.
 * You cannot trade with yourself or with a player who has been eliminated or is protected.
 * </p>
 */
public class KingCard extends Card {
    final String name = "King Arnaud IV";
    final int value = 6;
    final String description = "The undisputed ruler of Tempest… for the moment. Because of his role in the arrest " +
            "of Queen Marianna, he does not rate as highly with Princess Annette as a father should. He hopes to " +
            "work himself back into her graces.";
    final String function = "When you discard King Arnaud IV, trade the card in your hand with the card held by " +
            "another player of your choice. You cannot trade with a player who is out of the round.";
    final int amount = 1;
    GameSession gameSession;

    /**
     * Constructs a new KingCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public KingCard(GameSession gameSession) {
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
     * Plays the King card without a specified target.
     * <p>
     * This implementation simply prints the card name to the console.
     * </p>
     */
    public void play() {
        System.out.println(this.name);
        return;
    }

    /**
     * Plays the King card targeting by player index.
     * <p>
     * Not supported for KingCard; no action is taken.
     * </p>
     *
     * @param target the index of the target player (unused)
     */
    public void play(int target) {
    }

    /**
     * Plays the King card targeting a specific player by name.
     * <p>
     * If the target is invalid, eliminated, the same as the current player, or protected,
     * returns an error or info message. Otherwise, the current player and the target
     * exchange the single card they each hold.
     * </p>
     *
     * @param targetName the name of the player to trade with
     * @return a message describing the result of the trade or an error/info notice
     */
    public String play(String targetName) {
        var target = gameSession.getPlayer(targetName);
        var currentPlayer = gameSession.getCurrentPlayer();

        if (target == null || target.isEliminated()) {
            return "[ERROR] Invalid or eliminated target.";
        }

        if (target.getName().equals(currentPlayer.getName())) {
            return "[ERROR] You cannot trade with yourself.";
        }
        if(target.isProtected()){
            return target.getName() +" is protected.";
        }
        currentPlayer.increaseDiscardedCardsNumber(6);
        // Exchanging both players' only hand
        var yourCard = currentPlayer.getHand().remove(0);
        var targetCard = target.getHand().remove(0);

        currentPlayer.addCard(targetCard);
        target.addCard(yourCard);

        return "[King Effect] " + currentPlayer.getName() + " traded cards with " + target.getName();
    }
}