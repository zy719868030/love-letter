package de.lmu.gamepackage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the state and flow of a Love Letter game session.
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Tracking players, scores, and host</li>
 *   <li>Managing the deck, discard pile, and cards set aside</li>
 *   <li>Determining turn order, round winners, and overall game winner</li>
 *   <li>Handling round resets and game resets</li>
 * </ul>
 * </p>
 */
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
    private Card cardAside;
    private String lastRoundWinner;

    /**
     * Returns whether a session has been created.
     * @return true if create() has been called, false otherwise
     */
    public boolean isCreated() {
        return created;
    }

    /**
     * Marks which cards have been removed from play at the start of a round.
     * @param cardsAside list of cards to set aside
     */
    public void setCardsAside(List<Card> cardsAside) {
        this.cardsAside = cardsAside;
    }

    /**
     * Sets the single secret card aside.
     * @param cardAside the card removed face-down
     */
    public void setCardAside(Card cardAside) {
        this.cardAside = cardAside;
    }

    /**
     * Retrieves the card set aside at round start.
     * @return the face-down card
     */
    public Card getCardAside() {
        return  cardAside;
    }

    /**
     * Returns whether the game has started.
     * @return true after start() has been called, false otherwise
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Gets the set of current Player objects in this session.
     * @return set of Player
     */
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * Finds a Player by their nickname.
     * @param nickname the player’s unique name
     * @return the matching Player, or null if none found
     */
    public Player getPlayer(String nickname) {
        for (Player p : players) {
            if (p.getName().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the nicknames of all players in this session.
     * @return set of player nicknames
     */
    public Set<String> getPlayersNickname() {
        Set<String> playersNicknames = new HashSet<>();
        for (Player player : players) {
            playersNicknames.add(player.getName());
        }
        return playersNicknames;
    }

    /**
     * Retrieves the current scoreboard.
     * @return map from player nickname to their score
     */
    public Map<String, Integer> getScores() {
        return scores;
    }

    /**
     * Gets the nickname of the session creator.
     * @return host nickname
     */
    public String getHost() {
        return host;
    }

    /**
     * Initializes a new game session, clears all old data,and makes the creator the first player.
     * @param creator the nickname of the host
     */
    public void create(String creator) {
        this.created = true;
        this.started = false;
        this.players.clear();
        this.scores.clear();
        this.host = creator;

        addPlayer(creator);
    }

    /**
     * Adds a new player to the session.
     * @param nickname unique player name
     * @return always true
     */
    public boolean addPlayer(String nickname) {
        Player newPlayer = new Player(nickname);
        players.add(newPlayer);
        scores.putIfAbsent(nickname, 0);
        return true;
    }

    /**
     * Checks if all other players are protected by Handmaid.
     * @return true if every player except currentPlayer is protected
     */
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

    /**
     * Starts the round: shuffles deck, determines turn order based on
     * last date or previous winner, deals one card to each player.
     * @param playerNames     nicknames of players in play
     * @param lastRoundWinner nickname of the previous round’s winner (or null)
     * @param lastDatingDate  map of players’ most recent date
     * @param birthDate       map of players’ birthdays
     */
    public void start(Set<String> playerNames, String lastRoundWinner,Map<String, LocalDate> lastDatingDate,
        Map<String, LocalDate> birthDate) {
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

        if(lastRoundWinner == null) {
            turnOrder.offer(this.getPlayer(getInitialPlayer(lastDatingDate,birthDate)));
            for (String name : playerNames) {
                for (Player p : players) {
                    if (p.getName().equals(name) && !p.getName().equals(getInitialPlayer(lastDatingDate,birthDate))) {
                        turnOrder.offer(p);
                    }
                }
            }
        }
        else{
            turnOrder.offer(this.getPlayer(lastRoundWinner));
            for (String name : playerNames) {
                for (Player p : players) {
                    if (p.getName().equals(name) && !p.getName().equals(lastRoundWinner)) {
                        turnOrder.offer(p);
                    }
                }
            }
        }

        this.currentPlayer = turnOrder.peek();
        //draw card for each player
        for (Player p : turnOrder) {
            p.drawCard(deck.draw());
        }
    }

    /**
     * Draws one card for the current player.
     */
    public void currentPlayerDrawCard(){
        currentPlayer.drawCard(deck.draw());
    }

    /**
     * @return list of cards set aside at round start
     */
    public List<Card> getCardsAside() {
        return cardsAside;
    }

    /**
     * @return nickname of the player whose turn it is
     */
    public String getCurrentPlayerName() {
        return currentPlayer != null ? currentPlayer.getName() : null;
    }

    /**
     * Sets the current player directly.
     * @param player the Player to set as current
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * @return the Player whose turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //Method of adding players to the rotation
    /**
     * Advances turn order: moves the head of the queue to its tail,clears the previous player’s Handmaid protection,
     * and returns the next player’s nickname.
     * @return next player’s nickname, or null if no players remain
     */
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

    /**
     * Eliminates a player mid-round: removes them from turn order and marks them eliminated.
     * @return a List of player nicknames still in the round
     */
    public List<String> getRemainingPlayers() {
        List<String> remainingPlayers = new ArrayList<>();
        for (Player p : players) {
            if (!p.isEliminated()) {
                remainingPlayers.add(p.getName());
            }
        }
        return remainingPlayers;
    }

    /**
     * Resets player state but keeps scores and players list for next round.
     * @param lastRoundWinner nickname of the round just finished
     */
    public void resetRound(String lastRoundWinner) {
        this.started = false;
        for(Player p : players){
            p.setEliminated(false);
            p.resetHand();
            p.resetisOut();
            p.resetDiscardedCardsNumber();
        }
    }

    /**
     * Completely resets the game session (clears players and scores).
     */
    public void resetGame(){
        this.created = false;
        this.started = false;
        this.players.clear();
        this.scores.clear();
    }

    /**
     * Records who won the last round for turn order next round.
     * If multiple winners, uses date/birth tie-break rules.
     * @param lastRoundWinners list of round winners
     * @param lastDatingDate   map of players’ most recent date
     * @param birthDate        map of players’ birthdays
     */
    public void setLastRoundWinner(List<String> lastRoundWinners, Map<String, LocalDate> lastDatingDate,
                                   Map<String, LocalDate> birthDate) {
        if(lastRoundWinners.size()==1){
            this.lastRoundWinner = lastRoundWinners.getFirst();
        }
        else{
            List<String> initialPlayers = new ArrayList<>();
            LocalDate farestDate= LocalDate.MIN;
            for(String player: lastDatingDate.keySet()){
                if(lastDatingDate.get(player).isAfter(farestDate)){
                    initialPlayers.clear();
                    farestDate = lastDatingDate.get(player);
                    initialPlayers.add(player);
                }
                else if(lastDatingDate.get(player).isEqual(farestDate)){
                    initialPlayers.add(player);
                }
            }
            if(initialPlayers.size() == 1){
                this.lastRoundWinner = initialPlayers.getFirst();
            }
            else{
                LocalDate latestDate= LocalDate.MIN;
                String youngestPlayer = "";
                for(String player: initialPlayers){
                    if(birthDate.get(player).isAfter(latestDate) ||
                            birthDate.get(player).isEqual(latestDate)){
                        latestDate = birthDate.get(player);
                        youngestPlayer = player;
                    }
                }
                this.lastRoundWinner = youngestPlayer;
            }
        }
    }

    /**
     * Determines which player goes first when no prior winner exists, by latest date of last appointment, then by
     * youngest age on tie.
     * @param lastDatingDate map of players’ most recent date
     * @param birthDate      map of players’ birthdays
     * @return nickname of the initial player
     */
    public String getInitialPlayer(Map<String, LocalDate> lastDatingDate,
                                   Map<String, LocalDate> birthDate) {
        List<String> initialPlayers = new ArrayList<>();
        LocalDate farestDate= LocalDate.MIN;
        for(String player: lastDatingDate.keySet()){
            if(lastDatingDate.get(player).isAfter(farestDate)){
                initialPlayers.clear();
                farestDate = lastDatingDate.get(player);
                initialPlayers.add(player);
            }
            else if(lastDatingDate.get(player).isEqual(farestDate)){
                initialPlayers.add(player);
            }
        }
        if(initialPlayers.size() == 1){
            return initialPlayers.getFirst();
        }
        else{
            LocalDate latestDate= LocalDate.MIN;
            String youngestPlayer = "";
            for(String player: initialPlayers){
                if(birthDate.get(player).isAfter(latestDate) ||
                        birthDate.get(player).isEqual(latestDate)){
                    latestDate = birthDate.get(player);
                    youngestPlayer = player;
                }
            }
            return youngestPlayer;
        }
    }

    /**
     * @return true if the deck has no more cards to draw
     */
    public boolean isDeckEmpty() {
        return deck.isEmpty(); // assumes you have a deck object with isEmpty()
    }

    /**
     * Determines winner(s) when the deck is empty:
     * highest hand value wins; ties broken by sum of discarded card values.
     * @return list of winner nicknames (one or more)
     */
    public List<String> getWinnerByHand() {
        int maxnumber = 0;
        List<String> winner = new ArrayList<>();
        for (Player p : players) {
            if(!p.isEliminated()){
                if(p.getHand().getFirst().getValue() > maxnumber) {
                    maxnumber = p.getHand().getFirst().getValue();
                    winner.clear();
                    winner.add(p.getName());
                }
                else if(p.getHand().getFirst().getValue() == maxnumber){
                    winner.add(p.getName());
                }
            }
        }
        if(winner.size() == 1){
            return winner;
        }
        else{
            maxnumber = 0;
            List<String> candidates = new ArrayList<>();
            for (String candidate : winner) {
                candidates.add(candidate);
            }
            winner.clear();

            for (String candidate : candidates) {
                if (this.getPlayer(candidate).getDiscardedCards()> maxnumber) {
                    maxnumber = this.getPlayer(candidate).getDiscardedCards();
                    winner.clear();
                    winner.add(candidate);
                } else if (this.getPlayer(candidate).getDiscardedCards() == maxnumber) {
                    winner.add(candidate);
                }
            }
            return winner;
        }
    }

    /**
     * Increments each listed player’s score by one.
     * @param playerName list of nicknames to award points
     */
    public void incrementScore(List<String> playerName) {
        for (String name : playerName) {
            scores.merge(name, 1, Integer::sum);
        }
    }

    /**
     * @return nickname of the overall game winner, or null if none yet
     */
    public String getGameWinner() {
        int scoreToWin = switch (players.size()) {
            case 2 -> 7;
            case 3 -> 5;
            default -> 4;
        };

        for (Player p : players) {
            if (this.scores.get(p.getName())>= scoreToWin) {
                return p.getName();
            }
        }
        return null;
    }

    /**
     * @return the game’s deck instance
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * @return the nickname of last round’s winner
     */
    public String getLastRoundWinner() {
        return lastRoundWinner;
    }
}