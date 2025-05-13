package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;

public class KingCard extends Card {
    final String name = "King Arnaud IV";
    final int value = 6;
    final String description = "The undisputed ruler of Tempest… for the moment. Because of his role in the arrest " +
            "of Queen Marianna, he does not rate as highly with Princess Annette as a father should. He hopes to " +
            "work himself back into her graces.";
    final String function = "When you discard King Arnaud IV, trade the card in your hand with the card held by " +
            "another player of your choice. You cannot trade with a player who is out of the round.";
    final int amount = 1;
    GameSession gameSession;

    public KingCard(GameSession gameSession) {
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
        var target = gameSession.getPlayer(targetName);
        var currentPlayer = gameSession.getCurrentPlayer();

        if (target == null || target.isEliminated()) {
            return "[ERROR] Invalid or eliminated target.";
        }

        if (target.getName().equals(currentPlayer.getName())) {
            return "[ERROR] You cannot trade with yourself.";
        }
        if(target.isProtected()){
            return target.getName() +" is protected.";
        }

        // Exchanging both players' only hand
        var yourCard = currentPlayer.getHand().remove(0);
        var targetCard = target.getHand().remove(0);

        currentPlayer.addCard(targetCard);
        target.addCard(yourCard);

        return "[King Effect] " + currentPlayer.getName() + " traded cards with " + target.getName();
    }
}