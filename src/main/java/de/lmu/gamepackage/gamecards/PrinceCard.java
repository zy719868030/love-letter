package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

/**
 * Represents the “Prince” card (Prince Arnaud) in the game.
 * <p>
 * When this card is played or discarded, you choose one player (including yourself)
 * who must discard their hand (without triggering its effect, unless it is the Princess)
 * and then draw a new card. If the deck is empty, the player draws the card set aside
 * at the start of the round. If the discarded card is the Princess, the target is eliminated.
 * If all other players are protected by the Handmaid, you must choose yourself.
 * </p>
 */
public class PrinceCard extends Card {
    final String name = "Prince Arnaud";
    final int value = 5;
    final String description = "As a social gadfly, Prince Arnaud was not as distressed over his mother’s arrest as " +
            "one would suppose. Since many women clamor for his attention, he hopes to help his sister find the same " +
            "banal happiness by playing matchmaker.";
    final String function = "When you discard Prince Arnaud, choose one player still in the round (including " +
            "yourself). That player discards his or her hand (but doesn’t apply its effect, unless it is the " +
            "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot draw a card, that " +
            "player draws the card that was removed at the start of the round. If all other players are protected by " +
            "the Handmaid, you must choose yourself.";
    final int amount = 2;
    GameSession gameSession;

    /**
     * Constructs a new PrinceCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public PrinceCard(GameSession gameSession) {
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
     * Executes the Prince card effect without specifying a target.
     * <p>
     * For PrinceArnaud, playing without a target simply logs the card name.
     * </p>
     */
    public void play() {
        System.out.println(this.name);
        return;
    }

    /**
     * Executes the Prince card effect targeting by player index.
     * <p>
     * This method is not used; target selection by index is not supported.
     * </p>
     *
     * @param target the index of the target player (unused)
     */
    public void play(int target) {
    }

    /**
     * Executes the Prince card effect targeting a player by name.
     * <p>
     * The chosen player discards their hand (without triggering its effect unless
     * it is the Princess) and draws a replacement card. If the discarded card is
     * the Princess, the target is eliminated. If the deck is empty, the player
     * draws the card set aside at the start of the round.
     * </p>
     *
     * @param targetName the name of the player to target
     * @return a message describing the actions taken and the result
     */
    public String play(String targetName) {
        Player target = gameSession.getPlayer(targetName);
        if (target == null || target.isEliminated()) {
            return "[ERROR] Invalid or eliminated target.";
        }
        gameSession.getCurrentPlayer().increaseDiscardedCardsNumber(5);
        Card discarded = target.getHand().remove(0);
        target.increaseDiscardedCardsNumber(discarded.getValue());
        String returnMsg = "";
        returnMsg = "[Prince Effect] " + target.getName() + " discarded: " + discarded.getName();

        // Special case: Princess = elimination
        if (discarded.getName().equalsIgnoreCase("Princess Annette")) {
            target.setEliminated(true);
            returnMsg += "\n" + "[RESULT] " + target.getName() + " discarded the Princess and is eliminated!";
            return returnMsg;
        }

        // Draw a replacement card
        Card newCard;
        if (!gameSession.getDeck().isEmpty()) {
            newCard = gameSession.getDeck().draw();
            target.addCard(newCard);
        }
        else {
            target.addCard(gameSession.getCardAside());
            returnMsg += "\n No cards left to draw, so draw the card that were removed at the start of the round.";
        }

        returnMsg += "\n[INFO] " + target.getName() + " drew a new card.";
        return returnMsg;
    }

}
