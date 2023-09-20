package com.swen.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class LoadbalancerRunnable implements Runnable {

    private int port;

    public LoadbalancerRunnable(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(this.port);
                Socket socket = serverSocket.accept();
                InputStream fromClient = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromClient);
                BufferedReader brFromClient = new BufferedReader(reader);

                OutputStream toClient = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toClient);
                PrintWriter writeToClient = new PrintWriter(outputStreamWriter)) {

            System.out.println("Connection established...");
            String line = null;
            String responseToClient = null;

            Random random1 = new Random();
            int originalAnswer = random1.nextInt(100);
            int guessValue = 0;
            int guessAttempt = 0;

            while ((line = brFromClient.readLine()) != null) {
                String[] tokens = line.split(" ");

                String command;
                if (tokens.length == 1)
                    command = tokens[0];
                else if (tokens.length == 2) {
                    command = tokens[0];
                    guessValue = Integer.valueOf(tokens[1]);
                } else {
                    command = "invalid";
                }

                switch (command) {
                    case "restart": {
                        Random random2 = new Random();
                        originalAnswer = random2.nextInt(100);
                        guessAttempt = 0;
                        responseToClient = "restarted";
                        System.out.println("game restarted");
                    }
                        break;

                    case "guess": {
                        guessAttempt++;

                        if (guessAttempt > 6) {
                            responseToClient = "out_of_guesses";

                        } else {
                            if (guessValue < originalAnswer) {
                                responseToClient = "toolow";
                            } else if (guessValue > originalAnswer) {
                                responseToClient = "toohigh";
                            } else if (guessValue == originalAnswer) {
                                responseToClient = "correct";
                            }
                        }
                    }
                        break;

                    case "quit":
                        responseToClient = "game_over";
                        break;

                    default:
                        responseToClient = "error";
                        break;
                }
                System.out.println(responseToClient);
                writeToClient.println(responseToClient);
                writeToClient.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}