package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

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

    public void play() {
        throw new UnsupportedOperationException("Priest requires a target to play.");
    }

    public void play(int targetIndex) {
        throw new UnsupportedOperationException("Priest uses named target, not index.");
    }

    public String play(String targetName) {
        Player target = gameSession.getPlayer(targetName);
        Player current = gameSession.getCurrentPlayer();
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
        return target.getName() +"'s card:"+ target.getHand().get(0).getName();
    }
}