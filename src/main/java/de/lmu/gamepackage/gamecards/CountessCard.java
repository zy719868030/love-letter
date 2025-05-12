package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

public class CountessCard extends Card {
    final String name = "Countess Wilhelmina";
    final int value = 7;
    final String description = "Always on the prowl for a handsome man or juicy gossip, Wilhelmina’s age and noble " +
            "blood make her one of Princess Annette’s friends. While she has great influence over the Princess, she " +
            "makes herself scarce whenever the King or Prince are around.";
    final String function = "Unlike other cards, which take effect when discarded, the text on the Countess applies " +
            "while she is in your hand. In fact, the only time it doesn’t apply is when you discard her. \n" +
            "If you ever have the Countess and either the King or Prince in your hand, you must discard the Countess." +
            " You do not have to reveal the other card in your hand. Of course, you can also discard the Countess " +
            "even if you do not have a royal family member in your hand. The Countess likes to play mind games....";
    final int amount = 1;
    GameSession gameSession;

    public CountessCard(GameSession gameSession) {
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
        System.out.println("[Countess Effect] Countess has no active effect when played.");
    }

    public void play(int target) {
        throw new UnsupportedOperationException("Countess does not target any player.");
    }

    public void play(String targetName) {
        throw new UnsupportedOperationException("Countess does not target any player.");
    }
}
