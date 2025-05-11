package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

public class PrincessCard extends Card {
    final String name = "Princess Annette";
    final int value = 8;
    final String description = "Hampered only by the naïveté of youth, Princess Annette is elegant, charming, and " +
            "beautiful. Obviously, you want the princess to carry your letter. However, she is self-conscious about " +
            "matters of the heart, and if confronted, will toss your letter in the fire and deny looking at any " +
            "correspondence.";
    final String function = "If you discard the Princess—no matter how or why—she has tossed your letter into the " +
            "fire. You are immediately knocked out of the round. If the Princess was discarded by a card effect, any " +
            "remaining effects of that card do not apply (you do not draw a card from the Prince, for example). " +
            "Effects tied to being knocked out the round still apply (eg. Constable, Jester), however.";
    final int amount = 1;
    GameSession gameSession;

    public PrincessCard(GameSession gameSession) {
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

}
