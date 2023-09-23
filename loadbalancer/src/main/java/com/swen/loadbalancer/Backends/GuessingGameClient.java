package com.swen.loadbalancer.Backends;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GuessingGameClient {

    public static void main(String[] args) {
        // backend server 1
        String hostname;
        if (args.length == 1)
            hostname = args[0];
        else
            hostname = "localhost";

        try (Socket server = new Socket(hostname, 6001);
                InputStream fromServer = server.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromServer);
                BufferedReader brFromServer = new BufferedReader(reader);

                OutputStream toServer = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
                PrintWriter serverWriter = new PrintWriter(outputStreamWriter)) {

            System.out.println("This is a guessing game, you have to guess a number between 1-100 in 6 attempts");
            System.out.println("Commands to play the game \n1. guess [number] \n2. restart \n3. quit");
            System.out.println("enter your guesses [press enter] :");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            String line;
            do {
                serverWriter.println(input);
                serverWriter.flush();
                line = brFromServer.readLine();
                System.out.println(line);

                if (line.equalsIgnoreCase("correct")) {

                    System.out.println("You won. Congratulations.");
                    System.out.println("To play again enter restart [press enter]");
                    System.out.println("To quit playing enter quit [press enter]");
                    input = scanner.nextLine();

                } else if (line.equalsIgnoreCase("game_over")) {

                    System.out.println("Ending game...");
                    System.exit(1);
                    scanner.close();

                } else {

                    System.out.println("Try again : ");
                    input = scanner.nextLine();
                }

            } while (line != null);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
}