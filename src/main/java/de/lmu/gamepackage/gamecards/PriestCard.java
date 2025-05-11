package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

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

    public void play(){
        System.out.println(this.name);
        return;
    }

    public void play(int target) {
    }

}