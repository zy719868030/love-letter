package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

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

    public void play() {
        System.out.println(this.name);
        return;
    }

    public void play(int target) {
    }

    public String play(String targetName) {
        return null;
    }
    public String play(String targetName, String guessedCardName) {
        Player target = gameSession.getPlayer(targetName);

        if (target == null || target.isEliminated()) {
            return "[ERROR] Invalid or eliminated target.";
        }
        if(target.isProtected()){
            return target.getName() +" is protected.";
        }

        Card targetCard = target.getHand().get(0);
        if (targetCard.getValue() == Integer.parseInt(guessedCardName)) {
            target.setEliminated(true);
            return "Correct guess! " + target.getName() + " is eliminated.";

        } else {
            return "Wrong guess.";
        }
    }



}
