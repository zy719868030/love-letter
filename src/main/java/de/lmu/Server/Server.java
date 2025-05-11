package de.lmu.Server;

import de.lmu.gamepackage.GameMove;
import de.lmu.gamepackage.GameSession;

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

                            for (Map.Entry<String, PrintWriter> entry : clientWriters.entrySet()) {
                                if (!entry.getKey().equals(nickname)) {
                                    entry.getValue().println("Game has been created by <" + nickname + ">.");
                                    entry.getValue().println("<" + nickname + "> has joined the game.");
                                }
                            }
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

                            for (Map.Entry<String, PrintWriter> entry : clientWriters.entrySet()) {
                                if (!entry.getKey().equals(nickname)) {
                                    entry.getValue().println("<" + nickname + "> has joined the game.");
                                }
                            }
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