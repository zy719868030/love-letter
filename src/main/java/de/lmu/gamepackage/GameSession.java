package de.lmu.gamepackage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class GameSession {
    private Set<Player> players = new HashSet<>();
    private Map<String, Integer> scores = new HashMap<>();
    private boolean created = false;
    private boolean started = false;
    private String host;
    private List<Card> cardsAside = new ArrayList<>();
    private Queue<Player> turnOrder = new LinkedList<>();
    private Player currentPlayer;
    private Deck deck;
    private final Map<String, LocalDate> lastDatingDate = new HashMap<>();
    private final Map<String, LocalDate> birthDate      = new HashMap<>();
    private Card cardAside;

    public void setLastDatingDate(String player, LocalDate date) {
        lastDatingDate.put(player, date);
    }
    public void setBirthDate(String player, LocalDate date) {
        birthDate.put(player, date);
    }


    public boolean isCreated() {
        return created;
    }

    public void setCardsAside(List<Card> cardsAside) {
        this.cardsAside = cardsAside;
    }

    public void setCardAside(Card cardAside) {
        this.cardAside = cardAside;
    }

    public boolean isStarted() {
        return started;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String nickname) {
        for (Player p : players) {
            if (p.getName().equals(nickname)) {
                return p;
            }
        }
        return null;
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
    public boolean allProtected(){
        for(Player player: players){
            if(!currentPlayer.equals(player)){
                if(!player.isProtected()){
                    return false;
                }
            }
        }
        return true;
    }

    public void start(Set<String> playerNames) {
        this.started = true;
//        List<Player> list = new ArrayList<>();
//        for (Player p : players) {
//            if (playerNames.contains(p.getName())) {
//                list.add(p);
//            }
//        }
        // Sort: the “later” the date of the most recent appointment,
        // or: if the same, the “later” the birthday (the younger the age).
//        list.sort((p1, p2) -> {
//            LocalDate d1 = lastDatingDate.get(p1.getName());
//            LocalDate d2 = lastDatingDate.get(p2.getName());
//            int cmp = d2.compareTo(d1);
//            if (cmp != 0) return cmp;
//            return birthDate.get(p2.getName())
//                    .compareTo(birthDate.get(p1.getName()));
//        });
        turnOrder.clear();
//        turnOrder.addAll(list);
        this.currentPlayer = turnOrder.peek();


        deck = new Deck(this);
        DiscardPile discardPile = new DiscardPile(this);

        for (String name : playerNames) {
            for (Player p : players) {
                if (p.getName().equals(name)) {
                    turnOrder.offer(p);
                }
            }
        }

        this.currentPlayer = turnOrder.peek();
        //draw card for each player
        for (Player p : turnOrder) {
            p.drawCard(deck.draw());
        }
    }

    public void currentPlayerDrawCard(){
        currentPlayer.drawCard(deck.draw());
    }

    public List<Card> getCardsAside() {
        return cardsAside;
    }

    public String getCurrentPlayerName() {
        return currentPlayer != null ? currentPlayer.getName() : null;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //Method of adding players to the rotation
    public String nextTurn() {
        if (turnOrder.isEmpty()) return null;

        // Clear the previous player's Handmaid protection
        turnOrder.offer(turnOrder.poll());
        currentPlayer = turnOrder.peek();
        return currentPlayer.getName();
    }

    //Elimination of players (removal from rotation)
    public void eliminatePlayer(String nickname) {
        turnOrder.removeIf(p -> p.getName().equals(nickname));
        if (currentPlayer != null && currentPlayer.getName().equals(nickname)) {
            currentPlayer = turnOrder.peek();
        }
        getPlayer(nickname).setEliminated(true);
    }

    public Set<String> getRemainingPlayers() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .map(Player::getName)
                .collect(Collectors.toSet());
    }

    public void reset() {
        this.created = false;
        this.started = false;
        this.players.clear();
        this.scores.clear();
        this.host = null;
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty(); // assumes you have a deck object with isEmpty()
    }

    public String getWinnerByHand() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .max(Comparator.comparingInt(p -> p.getHand().get(0).getValue()))  // assumes hand has 1 card
                .map(Player::getName)
                .orElse(null);
    }

    public void incrementScore(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                p.incrementScore();  // assumes Player has this method
            }
        }
    }

    public String getGameWinner() {
        int scoreToWin = switch (players.size()) {
            case 2 -> 7;
            case 3 -> 5;
            default -> 4;
        };

        for (Player p : players) {
            if (p.getScore() >= scoreToWin) {
                return p.getName();
            }
        }
        return null;
    }

    public Deck getDeck() {
        return this.deck;
    }



}