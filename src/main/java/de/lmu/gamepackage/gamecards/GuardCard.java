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

    public void play(String targetName, String guessedCardName) {
        Player target = gameSession.getPlayer(targetName);

        if (target == null || target.isEliminated()) {
            System.out.println("[ERROR] Invalid or eliminated target.");
            return;
        }

        if (guessedCardName.equalsIgnoreCase("Guard")) {
            System.out.println("[INFO] Guard cannot be guessed.");
            return;
        }

        Card targetCard = target.getHand().get(0);
        if (targetCard.getName().equalsIgnoreCase(guessedCardName)) {
            System.out.println("Correct guess! " + target.getName() + " is eliminated.");
            target.setEliminated(true);
        } else {
            System.out.println("Wrong guess.");
        }
    }

    public void play(String targetName) {
        throw new UnsupportedOperationException("Guard requires a guessed card to play.");
    }

}
