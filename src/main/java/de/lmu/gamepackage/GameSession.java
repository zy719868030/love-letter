package de.lmu.gamepackage;

import java.util.*;

public class GameSession {
    private Set<Player> players = new HashSet<>();
    private final Map<String, Integer> scores = new HashMap<>();
    private boolean created = false;
    private boolean started = false;
    private String host;
    private List<Card> cardsAside = new ArrayList<>();

    public boolean isCreated() {
        return created;
    }

    public void setCardsAside(List<Card> cardsAside) {
        this.cardsAside = cardsAside;
    }

    public boolean isStarted() {
        return started;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Set<String> getPlayersNickname() {
        Set<String> playersNicknames = new HashSet<>();
        for (Player player : players) {
            playersNicknames.add(player.getName());
        }
        return playersNicknames;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public String getHost() {
        return host;
    }

    public void create(String creator) {
        this.created = true;
        this.started = false;
        this.players.clear();
        this.scores.clear();
        this.host = creator;

        addPlayer(creator);
    }

    public boolean addPlayer(String nickname) {
        Player newPlayer = new Player(nickname);
        players.add(newPlayer);
        scores.putIfAbsent(nickname, 0);
        return true;
    }

    public void start(Set<String> players) {
        this.started = true;
        Deck deck = new Deck(this);
        DiscardPile discardPile = new DiscardPile(this);
    }

    public void reset() {
        this.created = false;
        this.started = false;
        this.players.clear();
        this.scores.clear();
        this.host = null;
    }

    public List<Card> getCardsAside() {
        return cardsAside;
    }
}