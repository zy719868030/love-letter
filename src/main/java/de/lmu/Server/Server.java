package de.lmu.Server;

import de.lmu.gamepackage.Card;
import de.lmu.gamepackage.GameMove;
import de.lmu.gamepackage.GameSession;
import de.lmu.gamepackage.Player;
import de.lmu.gamepackage.gamecards.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Map<String, PrintWriter> clientWriters = new ConcurrentHashMap<>();
        Set<String> nicknames = ConcurrentHashMap.newKeySet();
        Set<String> players = ConcurrentHashMap.newKeySet();
        System.out.println("Server started...");
        GameSession gameSession = new GameSession();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected.");

            new Thread(() -> handleClient(clientSocket, nicknames, clientWriters, gameSession)).start();
        }
    }

    //Send a message to all clients
    private static void broadcastAll(Map<String, PrintWriter> clientWriters, String message) {
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }

    //Broadcast to everyone but yourself.
    private static void broadcastOthers(String except, Map<String, PrintWriter> clientWriters, String message) {
        for (Map.Entry<String, PrintWriter> entry : clientWriters.entrySet()) {
            if (!entry.getKey().equals(except)) {
                entry.getValue().println(message);
            }
        }
    }

    // At the end of a player's turn, call this after card effects and nextTurn()
    private static boolean checkRoundEnd(GameSession gameSession, Map<String, PrintWriter> clientWriters) {
        // Rule 1: only one player left (others eliminated)
        if (gameSession.getRemainingPlayers().size() == 1) {
            String winner = gameSession.getRemainingPlayers().iterator().next();
            broadcastGameEvent(GameEventType.ROUND_END, clientWriters, winner);
            gameSession.incrementScore(winner);
            return true;
        }

        // Rule 2: deck is empty
        if (gameSession.isDeckEmpty()) {
            String winner = gameSession.getWinnerByHand();  // highest card wins
            broadcastGameEvent(GameEventType.ROUND_END, clientWriters, winner);
            gameSession.incrementScore(winner);
            return true;
        }

        return false; // round continues
    }

    // After detecting round end, check if game is won
    private static boolean checkGameEnd(GameSession gameSession, Map<String, PrintWriter> clientWriters) {
        String winner = gameSession.getGameWinner();
        if (winner != null) {
            broadcastGameEvent(GameEventType.GAME_END, clientWriters, winner);
            return true;
        }
        return false;
    }

    //Server notifies players of game events
    public enum GameEventType {
        TURN, ELIMINATED, ROUND_END, GAME_END
    }

    private static void broadcastGameEvent(GameEventType type, Map<String, PrintWriter> clientWriters, String... args) {
        String msg = switch (type) {
            case TURN -> "[GAME] It's now <" + args[0] + ">'s turn.";
            case ELIMINATED -> "[GAME] Player <" + args[0] + "> has been eliminated.";
            case ROUND_END -> "[GAME] Round over! <" + args[0] + "> wins the round.";
            case GAME_END -> "[GAME] Game over! <" + args[0] + "> wins the game!";
        };
        broadcastAll(clientWriters, msg);
    }

    private static void handleClient(Socket socket, Set<String> nicknames, Map<String, PrintWriter> clientWriters,
                                     GameSession gameSession) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Check if the nickname is unique, reject if repeated.
            String nickname = in.readLine();
            if (nicknames.contains(nickname)) {
                out.println("reject");
                socket.close();
                return;
            }

            //If not duplicated, send “accept” and “welcome” to the client.
            nicknames.add(nickname);
            clientWriters.put(nickname,out);
            out.println("accept");
            out.println("welcome " + nickname);
            out.println("Type </help> to view the available commands");

            //Broadcast “xxx joined the room” to the other clients.
            for (PrintWriter writer : clientWriters.values()) {
                if (writer != out) {
                    writer.println(nickname + " joined the room");
                }
            }

            //Continuously reads messages sent by clients and broadcasts them to all connected clients.
            String msg;
            while ((msg = in.readLine()) != null) {

                //Add help command to prompt users on how to use different commands
                if (msg.equalsIgnoreCase("/help")) {
                    out.println("Available commands:");
                    out.println("/r <user> <text>   - Send a private message to <user>");
                    out.println("bye                - Leave the chat");
                    out.println("/help              - Show this help message");
                    out.println("/create            - Create a new game session");
                    out.println("/join              - Join the current game session");
                    out.println("/start             - Start the game (host only)");
                    out.println("/score             - Show current game scores (if implemented)");
                    continue;
                }

                //Check if it's a private chat command.
                //Implement a private chat with the format “/msg <user> <text>”.
                if (msg.startsWith("/r ")) {
                    String[] parts = msg.split(" ", 3);
                    if (parts.length < 3) {
                        out.println("Error: Invalid format. Use: /r <user> <text>");
                    } else {
                        String target = parts[1];
                        String message = parts[2];
                        PrintWriter targetWriter = clientWriters.get(target);
                        if (targetWriter != null) {
                            targetWriter.println("[PM from " + nickname + "]: " + message);
                            out.println("[PM to " + target + "]: " + message);
                        } else {
                            out.println("Error: User " + target + " not found.");
                        }
                    }
                    continue;
                }

                //Create a game, the player who created the game automatically joins the game
                if (msg.equals("/create")) {
                    synchronized (gameSession) {
                        if (gameSession.isCreated()) {
                            out.println("Error: A game already exists.");
                        } else {
                            gameSession.create(nickname);
                            out.println("Game successfully created by <" + nickname + ">.");
                            out.println("You have automatically joined the game.");

                            broadcastOthers(nickname, clientWriters, "Game has been created by <" + nickname +
                                    ">.");
                            broadcastOthers(nickname, clientWriters, "<" + nickname + "> has joined the game.");
                        }
                    }
                    continue;
                }

                //join the game
                if (msg.equals("/join")) {
                    synchronized (gameSession) {
                        if (!gameSession.isCreated()) {
                            out.println("Error: No game has been created.");
                        } else if (gameSession.isStarted()) {
                            out.println("Error: Game has already started.");
                        } else if (gameSession.getPlayers().contains(nickname)) {
                            out.println("You have already joined the game.");
                        } else {
                            gameSession.addPlayer(nickname);
                            out.println("You have joined the game.");

                            broadcastOthers(nickname, clientWriters, "<" + nickname + "> has joined the game.");
                        }
                    }
                    continue;
                }

                //start the game
                if (msg.equals("/start")) {
                    synchronized (gameSession) {
                        if (!gameSession.isCreated()) {
                            out.println("Error: No game to start.");
                        } else if (gameSession.isStarted()) {
                            out.println("Error: Game has already started.");
                        } else if (gameSession.getPlayers().size() < 2) {
                            out.println("Error: At least 2 players are required to start.");
                        } else if (!gameSession.getHost().equals(nickname)) {
                            out.println("Error: Only the game creator can start the game.");
                        } else {
                            gameSession.start(nicknames);
                            String message = "Game started with players: " + gameSession.getPlayersNickname();
                            out.println(message);

                            //Indicates whose turn it is
                            String currentPlayer = gameSession.getCurrentPlayerName();
                            broadcastGameEvent(GameEventType.TURN, clientWriters, currentPlayer);

                            if(gameSession.getPlayers().size() == 2){
                                String cardsAsideMessage = "";
                                for (int i = 0; i<gameSession.getCardsAside().size(); i++) {
                                    cardsAsideMessage += "<" + gameSession.getCardsAside().get(i).getName() + "> ";
                                }
                                cardsAsideMessage+="are set aside.";
                                out.println(cardsAsideMessage);
                            }


                            for (Map.Entry<String, PrintWriter> entry : clientWriters.entrySet()) {
                                if (!entry.getKey().equals(nickname)) {
                                    entry.getValue().println(message);
                                }
                            }
                        }
                    }
                    continue;
                }

                if (msg.startsWith("/play ")) {
                    synchronized (gameSession) {
                        if (!gameSession.isCreated()) {
                            out.println("Error: No game in progress.");
                            continue;
                        }
                        if (!gameSession.isStarted()) {
                            out.println("Error: The game has not started yet.");
                            continue;
                        }
                        if (!gameSession.getCurrentPlayerName().equals(nickname)) {
                            out.println("It's not your turn.");
                            continue;
                        }
                        if (!gameSession.getPlayersNickname().contains(nickname)) {
                            out.println("You are not part of the game.");
                            continue;
                        }

                        Player player = gameSession.getPlayer(nickname);
                        if (player == null || player.isEliminated()) {
                            out.println("Error: Invalid player or already eliminated.");
                            continue;
                        }

                        // Example format: /play 0 or /play 1
                        String[] parts = msg.split(" ");
                        if (parts.length != 2) {
                            out.println("Usage: /play <0 or 1>");
                            continue;
                        }

                        int cardIndex;
                        try {
                            cardIndex = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            out.println("Usage: /play <0 or 1>");
                            continue;
                        }

                        if (cardIndex < 0 || cardIndex > 1 || player.getHand().size() <= cardIndex) {
                            out.println("Invalid card index.");
                            continue;
                        }

                        Card cardToPlay = player.getHand().get(cardIndex);
                        player.playCard(cardIndex); // Remove from hand
                        broadcastGameEvent(GameEventType.TURN, clientWriters, nickname + " played " +
                                cardToPlay.getName());
                        if (cardToPlay instanceof PrincessCard) {
                            broadcastGameEvent(GameEventType.ELIMINATED, clientWriters, nickname);
                            out.println("[RULE] You discarded the Princess and are eliminated from the round.");
                        }

                        // GuardCard
                        if (cardToPlay instanceof GuardCard guardCard) {
                            if (parts.length < 4) {
                                out.println("Usage: /play 0 <targetName> <guessCard>");
                                continue;
                            }
                            String targetName = parts[2];
                            String guess = parts[3];
                            guardCard.play(targetName, guess);
                        }

                        // PriestCard
                        else if (cardToPlay instanceof PriestCard priestCard) {
                            if (parts.length < 3) {
                                out.println("Usage: /play 0 <targetName>");
                                continue;
                            }

                            String targetName = parts[2];
                            priestCard.play(targetName);
                        }

                        // BaronCard
                        else if (cardToPlay instanceof BaronCard baronCard) {
                            if (parts.length < 3) {
                                out.println("Usage: /play 0 <targetName>");
                                continue;
                            }
                            String targetName = parts[2];
                            baronCard.play(targetName);
                        }

                        // HandmaidCard
                        else if (cardToPlay instanceof HandmaidCard handmaidCard) {
                            handmaidCard.play();  // 调用 play() 方法内会自动设置玩家 protected=true
                            out.println("You are protected until your next turn.");
                        }

                        // PrinceCard
                        else if (cardToPlay instanceof PrinceCard princeCard) {
                            if (parts.length < 3) {
                                out.println("Usage: /play 0 <targetName>");
                                continue;
                            }

                            princeCard.play(parts[2]);
                        }

                        // KingCard
                        else if (cardToPlay instanceof KingCard kingCard) {
                            if (parts.length < 3) {
                                out.println("Usage: /play 0 <targetName>");
                                continue;
                            }
                            kingCard.play(parts[2]);
                        }

                        // Countess
                        List<Card> hand = player.getHand();
                        boolean hasCountess = hand.stream().anyMatch(c -> c instanceof CountessCard);
                        boolean hasPrinceOrKing = hand.stream().anyMatch(c -> c instanceof PrinceCard || c instanceof KingCard);

                        if (hasCountess && hasPrinceOrKing && !(cardToPlay instanceof CountessCard)) {
                            out.println("[RULE] You must play the Countess if you have her with a King or Prince.");
                            continue;
                        }

                        // Default(PrincessCard)
                        else {
                            cardToPlay.play();
                        }

                        // Check round/game state
                        boolean roundOver = checkRoundEnd(gameSession, clientWriters);
                        if (roundOver) {
                            boolean gameOver = checkGameEnd(gameSession, clientWriters);
                            if (!gameOver) {
                                gameSession.reset();
                            }
                            continue;
                        }

                        // Go to next turn
                        String nextPlayer = gameSession.nextTurn();
                        broadcastGameEvent(GameEventType.TURN, clientWriters, nextPlayer);

                    }
                    continue;
                }


                //Show Player Score
                if (msg.equals("/score")) {
                    out.println("Current Scores:");
                    for (Map.Entry<String, Integer> entry : gameSession.getScores().entrySet()) {
                        out.println("<" + entry.getKey() + ">: " + entry.getValue() + " point(s)");
                    }
                    continue;
                }

                //Exits the loop when it receives a “bye”
                if (msg.equalsIgnoreCase("bye")) break;

                for (PrintWriter writer : clientWriters.values()) {
                    writer.println(nickname + ": " + msg);
                }
            }

            //Disconnect and broadcast user departure to other clients
            nicknames.remove(nickname);
            clientWriters.remove(nickname);
            socket.close();
            System.out.println(nickname + " disconnected.");
            for (PrintWriter writer : clientWriters.values()) {
                writer.println(nickname + " left the room");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//String received2 = in.readLine();
//ObjectMapper mapper = new ObjectMapper();
//GameMove gameMove = mapper.readValue(received2, GameMove.class);
//System.out.println(gameMove.figure() + "moved to" + gameMove.x() + "," + gameMove.y());