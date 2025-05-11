package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

public class HandmaidCard extends Card {
    final String name = "Handmaid Susannah";
    final int value = 4;
    final String description = "Few would trust a mere Handmaid with a letter of importance. Fewer still understand " +
            "Susannah’s cleverness, or her skilled ability at playing the foolish Handmaid. That the Queen’s " +
            "confidante and loyal servant escaped any attention after the Queen’s arrest is a testament to her " +
            "clever mind.";
    final String function = "When you discard the Handmaid, you are immune to the effects of other players’ cards " +
            "until the start of your next turn. If all players other than the player whose turn it is are protected " +
            "by the Handmaid, the player must choose him or herself for a card’s effects, if possible.";
    final int amount = 2;
    GameSession gameSession;

    public HandmaidCard(GameSession gameSession) {
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
