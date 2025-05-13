package de.lmu.gamepackage.gamecards;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;

public class PrinceCard extends Card {
    final String name = "Prince Arnaud";
    final int value = 5;
    final String description = "As a social gadfly, Prince Arnaud was not as distressed over his mother’s arrest as " +
            "one would suppose. Since many women clamor for his attention, he hopes to help his sister find the same " +
            "banal happiness by playing matchmaker.";
    final String function = "When you discard Prince Arnaud, choose one player still in the round (including " +
            "yourself). That player discards his or her hand (but doesn’t apply its effect, unless it is the " +
            "Princess, see page 8) and draws a new one. If the deck is empty and the player cannot draw a card, that " +
            "player draws the card that was removed at the start of the round. If all other players are protected by " +
            "the Handmaid, you must choose yourself.";
    final int amount = 2;
    GameSession gameSession;

    public PrinceCard(GameSession gameSession) {
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
            return "[ERROR] Invalid or eliminated target.";
        }

        Card discarded = target.getHand().remove(0);
        String returnMsg = "";
        returnMsg = "[Prince Effect] " + target.getName() + " discarded: " + discarded.getName();

        // Special case: Princess = elimination
        if (discarded.getName().equalsIgnoreCase("Princess Annette")) {
            target.setEliminated(true);
            returnMsg += "\n" + "[RESULT] " + target.getName() + " discarded the Princess and is eliminated!";
            return returnMsg;
        }

        // Draw a replacement card
        Card newCard;
        if (!gameSession.getDeck().isEmpty()) {
            newCard = gameSession.getDeck().draw();
            target.addCard(newCard);
        }
        else {
            returnMsg += "\n No cards left to draw, so draw the card that were removed at the start of the round.";
        }

        returnMsg += "\n [INFO] " + target.getName() + " drew a new card.";
        return returnMsg;
    }

}
