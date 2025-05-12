package de.lmu.gamepackage;

public abstract class Card {
    protected String name;
    protected int value;
    protected String description;
    protected String function;
    protected int amount;
    protected GameSession  gameSession;

//    public Card(String name, int value) {
//        this.name = name;
//        this.value = value;
//    }
    public Card(){};


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

    public abstract void play();                          // For no-target cards
    public abstract void play(int target);                // For index-based cards
    public abstract void play(String targetName);         // For name-targeted cards

    public void play(String targetName, String guessedCardName) {
        throw new UnsupportedOperationException("This card does not support guessing play.");
    }
}
