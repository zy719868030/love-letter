package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

/**
 * Represents the “Priest” card (Priest Tomas) in the game.
 * <p>
 * When this card is played, you choose another player and look at their hand
 * without revealing it to any other players.
 * </p>
 */
public class PriestCard extends Card {
    final String name = "Priest Tomas";
    final int value = 2;
    final String description = "Open, honest, and uplifting, Father Tomas always seeks out the opportunity to do " +
            "good. With the arrest of the Queen, he is often seen about the palace, acting as confessor, counselor, " +
            "and friend.";
    final String function = "When you discard the Priest, you can look at another player’s hand. Do not reveal the " +
            "hand to any other players.";
    final int amount = 2;
    GameSession gameSession;

    /**
     * Constructs a new PriestCard tied to the given game session.
     *
     * @param gameSession the current game session context
     */
    public PriestCard(GameSession gameSession){
        this.gameSession=gameSession;
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
     * Playing the Priest without specifying a target always requires one.
     *
     * @throws UnsupportedOperationException always thrown to indicate a target is required
     */
    public void play() {
        throw new UnsupportedOperationException("Priest requires a target to play.");
    }

    /**
     * Playing the Priest by numeric index is not supported.
     *
     * @param targetIndex the index of the target player (unused)
     * @throws UnsupportedOperationException always thrown to indicate named targets only
     */
    public void play(int targetIndex) {
        throw new UnsupportedOperationException("Priest uses named target, not index.");
    }

    /**
     * Executes the Priest card effect targeting a player by name.
     * <p>
     * If the target is protected or invalid, returns an appropriate message.
     * Otherwise, increases the current player's discard count, checks protection,
     * and if valid, reveals the target’s card value and name in a private message.
     * </p>
     *
     * @param targetName the name of the player to inspect
     * @return a message with the inspected card or an error/protection notice
     */
    public String play(String targetName) {
        Player target = gameSession.getPlayer(targetName);
        Player current = gameSession.getCurrentPlayer();
        gameSession.getCurrentPlayer().increaseDiscardedCardsNumber(2);
        if(target.isProtected()){
            return target.getName() +" is protected.";
        }

        if (target == null || target.isEliminated()) {
            System.out.println("[ERROR] Invalid or eliminated target.");
            return null;
        }

        if (target.getHand().isEmpty()) {
            System.out.println("[INFO] Target has no cards.");
            return null;
        }

//        System.out.println("[Priest View] You (" + current.getName() + ") see <" + target.getName()
//                + ">'s card: " + target.getHand().get(0).getName());
        return target.getName() +"'s card: [" + target.getHand().get(0).getValue() + ":" + target.getHand().get(0).
                getName() + "]";
    }
}