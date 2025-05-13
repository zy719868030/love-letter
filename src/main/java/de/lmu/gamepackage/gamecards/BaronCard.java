package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

/**
 * Represents the “Baron” card (Baron Talus) in the game.
 * <p>
 * When this card is discarded, you choose another player still in the round.
 * You and that player secretly compare the values of your cards.
 * The player with the lower value is knocked out of the round;
 * in case of a tie, nothing happens.
 * </p>
 */
public class BaronCard extends Card {
    final String name = "Baron Talus";
    final int value = 3;
    final String description = "The scion of an esteemed house that has long been a close ally of the royal family, " +
            "Baron Talus has a quiet and gentle demeanor that conceals a man used to being obeyed. His suggestions " +
            "are often treated as if they came from the King himself.";
    final String function = "When you discard the Baron, choose another player still in the round. You and that " +
            "player secretly compare your hands. The player with the lower number is knocked out of the round. In " +
            "case of a tie, nothing happens.";
    final int amount = 2;
    GameSession gameSession;

    /**
     * Constructs a new BaronCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public BaronCard(GameSession gameSession) {
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
     * Plays the Baron card without specifying a target.
     * <p>
     * This base implementation simply logs the card name.
     * </p>
     */
    public void play() {
        System.out.println(this.name);
        return;
    }


    public void play(int target) {
    }

    /**
     * Executes the Baron card effect targeting a specific player by name.
     * <p>
     * If the target is invalid or already eliminated, returns an error notice.
     * If the target is protected, returns an info notice.
     * Otherwise, both players compare their card values: the lower is eliminated;
     * on a tie, nothing happens. Both discards count toward scoring.
     * </p>
     *
     * @param targetName the name of the player to compare hands with
     * @return a message describing the comparison and result, or an error/info notice
     */
    public String play(String targetName) {
        Player target = gameSession.getPlayer(targetName);

        if (target == null || target.isEliminated()) {
            System.out.println("[ERROR] Invalid or eliminated target.");
            return null;
        }

        if(target.isProtected()){
            return target.getName() +" is protected.";
        }

        Player currentPlayer = gameSession.getCurrentPlayer();
        Card yourCard = currentPlayer.getHand().get(0);
        Card targetCard = target.getHand().get(0);

        String info = "[INFO] " + currentPlayer.getName() + " (" + yourCard.getName() + ") vs "
                + target.getName() + " (" + targetCard.getName() + ")" + '\n';

        currentPlayer.increaseDiscardedCardsNumber(3);
        target.increaseDiscardedCardsNumber(3);

        if (yourCard.getValue() > targetCard.getValue()) {
            target.setEliminated(true);
            return info + "[RESULT] " + target.getName() + " is eliminated.";
        } else if (yourCard.getValue() < targetCard.getValue()) {
            currentPlayer.setEliminated(true);
            return info + "[RESULT] " + currentPlayer.getName() + " is eliminated.";
        } else {
            return info + "[RESULT] It's a tie. Nothing happens.";
        }
    }
}
