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

    public void play(String targetName) {
        Player target = gameSession.getPlayer(targetName);
        if (target == null || target.isEliminated()) {
            System.out.println("[ERROR] Invalid or eliminated target.");
            return;
        }

        Card discarded = target.getHand().remove(0);
        System.out.println("[Prince Effect] " + target.getName() + " discarded: " + discarded.getName());

        // Special case: Princess = elimination
        if (discarded.getName().equalsIgnoreCase("Princess Annette")) {
            System.out.println("[RESULT] " + target.getName() + " discarded the Princess and is eliminated!");
            target.setEliminated(true);
            return;
        }

        // Draw a replacement card
        Card newCard;
        if (!gameSession.getDeck().isEmpty()) {
            newCard = gameSession.getDeck().draw();
        } else if (!gameSession.getCardsAside().isEmpty()) {
            newCard = gameSession.getCardsAside().remove(0); // Last resort
        } else {
            System.out.println("[WARNING] No cards left to draw.");
            return;
        }

        target.addCard(newCard);
        System.out.println("[INFO] " + target.getName() + " drew a new card.");
    }

}
