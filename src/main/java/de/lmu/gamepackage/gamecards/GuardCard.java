package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

/**
 * Represents the “Guard” card (Guard Odette) in the game.
 * <p>
 * When this card is played, you choose another player and name a number (other than 1).
 * If that player holds a card with the guessed value, they are eliminated from the round.
 * If no valid target is available (e.g., all others are protected), the card is discarded without effect.
 * </p>
 */
public class GuardCard extends Card {
    final String name = "Guard Odette";
    final int value = 1;
    final String description = "Charged with seeing to the security of the royal family, Odette follows her orders " +
            "with persistence and diligence… even though her mentor is said to have drowned while fleeing arrest for " +
            "complicity in the Queen’s treason.";
    final String function = "When you discard the Guard, choose a player and name a number (other than 1). If that " +
            "player has that number in their hand, that player is knocked out of the round. If all other players " +
            "still in the round cannot be chosen (eg. due to Handmaid or Sycophant), this card is discarded without " +
            "effect.";
    final int amount = 5;
    GameSession gameSession;

    /**
     * Constructs a GuardCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public GuardCard(GameSession gameSession) {
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
     * Plays the Guard without specifying a target or guess.
     * <p>
     * This base implementation simply logs the card name.
     * </p>
     */
    public void play() {
        System.out.println(this.name);
        return;
    }

    /**
     * Playing the Guard by numeric index is not supported.
     *
     * @param target unused
     */
    public void play(int target) {
    }

    /**
     * Playing the Guard without a guess is not supported.
     *
     * @param targetName unused
     * @return never returns normally
     * @throws UnsupportedOperationException always thrown to indicate a guess is required
     */
    public String play(String targetName) {
        return null;
    }

    /**
     * Executes the Guard effect by targeting a player by name and guessing their card’s value.
     * <p>
     * If the guess matches the target’s hand value, the target is eliminated.
     * Otherwise, the guess fails and no one is eliminated.
     * </p>
     *
     * @param targetName      the name of the player to inspect
     * @param guessedCardName the guessed card value as a string (must not be "1")
     * @return a result message indicating success, failure, or any error/protection notice
     */
    public String play(String targetName, String guessedCardName) {
        Player target = gameSession.getPlayer(targetName);

        if (target == null || target.isEliminated()) {
            return "[ERROR] Invalid or eliminated target.";
        }
        if(target.isProtected()){
            return target.getName() +" is protected.";
        }
        gameSession.getCurrentPlayer().increaseDiscardedCardsNumber(1);
        Card targetCard = target.getHand().get(0);
        if (targetCard.getValue() == Integer.parseInt(guessedCardName)) {
            target.setEliminated(true);
            return "Correct guess! " + target.getName() + " is eliminated.";

        } else {
            return "Wrong guess.";
        }
    }



}
