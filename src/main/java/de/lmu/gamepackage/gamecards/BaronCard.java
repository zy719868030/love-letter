package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

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

    public void play() {
        System.out.println(this.name);
        return;
    }

    public void play(int target) {
    }

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
