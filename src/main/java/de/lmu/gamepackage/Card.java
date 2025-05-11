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

    public abstract void play(int target);
    public abstract void play();
}
