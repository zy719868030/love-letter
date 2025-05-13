package de.lmu.client;

import de.lmu.gamepackage.GameMove;
import de.lmu.gamepackage.GameSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A simple console‐based client for the Love Letter game server.
 * <p>
 * Connects to the server, handles user login (nickname, dating date, birthday),
 * and then enters a loop to send commands and print server responses.
 * </p>
 */
public class Client {

    /**
     * Entry point for the client application.
     * <ol>
     *   <li>Connects to the server at localhost:8080</li>
     *   <li>Prompts the user for a nickname and sends it</li>
     *   <li>If accepted, prompts for last appointment date and birthday (DD/MM/YYYY)</li>
     *   <li>Starts a background thread to listen for server messages</li>
     *   <li>Main thread reads console input and sends commands (e.g. /C, /J, /play, bye)</li>
     * </ol>
     * @param args ignored
     * @throws IOException if any socket or I/O error occurs
     */
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8080);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        //Check if the nickname is unique prompt the user to enter a nickname and send it to the server
        System.out.println("Enter nickname: ");
        String nickname = console.readLine();
        out.println(nickname);

        String response = in.readLine();
        System.out.println("You are " + response + "ed" );
        if(response.equals("accept")) {
            String welcomeMsg = in.readLine();
            System.out.println(welcomeMsg);

            String datingStr;
            while (true) {
                System.out.print("Enter the date of your last appointment (DD/MM/YYYY): ");
                datingStr = console.readLine();
                if (datingStr.matches("\\d{2}/\\d{2}/\\d{4}"))
                {
                    out.println(datingStr);
                    break;
                }

                System.out.println("Invalid format, please try again.");
            }

            String birthStr;
            while (true) {
                System.out.print("Enter your birthday (DD/MM/YYYY): ");
                birthStr = console.readLine();
                if (birthStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    out.println(birthStr);
                    break;
                }
                System.out.println("Invalid format, please try again.");
          }


            new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            while (true) {
                // Send the incoming message to the server
                String input = console.readLine();
                if (input.equalsIgnoreCase("bye")) {
                    out.println("bye");
                    break;
                }
                out.println(input);
            }
            socket.close();
        }else {
        System.out.println("Username is invalid or already taken. Connection closed.");
        }
    }
}


//GameMove gameMove = new GameMove("Ture", 2, 3);
//ObjectMapper mapper = new ObjectMapper();
//String gamePackage = mapper.writeValueAsString(gameMove);
//out.println(gamePackage);