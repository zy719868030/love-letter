package de.lmu.Server;


import de.lmu.gamepackage.GameMove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Server {

    public static void main(String[] args) throws IOException {

    ServerSocket serverSocket = new ServerSocket(8080);

    System.out.println("Waiting for connection...");
    Socket clientConnection = serverSocket.accept();
    System.out.println("Connection Accepted");

    BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
    String received = in.readLine();
    System.out.println(received);

    String received2 = in.readLine();
    ObjectMapper mapper = new ObjectMapper();
    GameMove gameMove = mapper.readValue(received2, GameMove.class);
    System.out.println(gameMove.figure() + "moved to" + gameMove.x() + "," + gameMove.y());
    }
}
