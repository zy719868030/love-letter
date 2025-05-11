package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

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

}
