package de.lmu.client;

import de.lmu.gamepackage.GameMove;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8080);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Hello Server!");

        GameMove gameMove = new GameMove("Ture", 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        String gamePackage = mapper.writeValueAsString(gameMove);
        out.println(gamePackage);
    }
}
