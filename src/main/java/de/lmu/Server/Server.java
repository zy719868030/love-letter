package de.lmu.Server;


import de.lmu.gamepackage.GameMove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;



public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        List<PrintWriter> clientWriters = new ArrayList<>();
        Set<String> nicknames = new HashSet<>();
        System.out.println("Server started...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected.");

            new Thread(() -> handleClient(clientSocket, nicknames, clientWriters)).start();
        }
    }

    private static void handleClient(Socket socket, Set<String> nicknames, List<PrintWriter> clientWriters) {
        try {
            // 1. 连接建立
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //System.out.println("Waiting for connection...");
            //Socket clientConnection = serverSocket.accept();
            //PrintWriter out = new PrintWriter(clientConnection.getOutputStream(), true);
            //System.out.println("Connection Accepted");

            //BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            //String nickname = in.readLine();
            //System.out.println(nickname);

            String nickname = in.readLine();
            if (nicknames.contains(nickname)) {
                out.println("reject");
                socket.close();
                return;
            }

            nicknames.add(nickname);
            clientWriters.add(out);
            out.println("accept");
            out.println("welcome " + nickname);

            //Check if the nickname is unique
            for (PrintWriter writer : clientWriters) {
                if (writer != out) {
                    writer.println(nickname + " joined the room");
                }
            }

            for (PrintWriter writer : clientWriters) {
                if (writer != out) {
                    writer.println(nickname + " joined the room");
                }
            }

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("bye")) break;

                for (PrintWriter writer : clientWriters) {
                    writer.println(nickname + ": " + msg);
                }
            }

            nicknames.remove(nickname);
            clientWriters.remove(out);
            socket.close();
            System.out.println(nickname + " disconnected.");

            for (PrintWriter writer : clientWriters) {
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