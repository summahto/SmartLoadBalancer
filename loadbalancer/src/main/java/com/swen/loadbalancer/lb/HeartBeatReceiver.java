package com.swen.loadbalancer.lb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HeartBeatReceiver implements Runnable {

    protected long lastUpdatedTime;

    private int port;
    private Backend backend;

    public HeartBeatReceiver(int port, Backend backend) {
        this.port = port;
        this.backend = backend;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(this.port);
                // server socket is created and awaits an incoming connection using
                // serverSocket.accept()
                Socket socket = serverSocket.accept();
                InputStream fromClient = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromClient);
                BufferedReader brFromClient = new BufferedReader(reader);

                OutputStream toClient = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toClient);
                PrintWriter writeToClient = new PrintWriter(outputStreamWriter)) {

            System.out.println("Connection established.. To heartbeat sender running on port : " + this.port);

            // Method to get heartbeat from the backends
            getHeartBeat(brFromClient);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void getHeartBeat(BufferedReader brFromClient) throws IOException {

        String line = null;
        String responseToClient = null;

        int value = 0;

        while ((line = brFromClient.readLine()) != null) {
            String[] tokens = line.split(" ");

            String command;
            if (tokens.length == 1)
                command = tokens[0];
            else if (tokens.length == 2) {
                command = tokens[0];
                value = Integer.valueOf(tokens[1]);
            } else {
                command = "invalid";
            }

            switch (command) {

                case "heartbeat":
                    updateTime(System.currentTimeMillis());
                    responseToClient = "Received your heartbeat, updating your status";
                    break;

                default:
                    responseToClient = "error";
                    break;
            }

            System.out.println(responseToClient);
            // writeToClient.println(responseToClient);
            // writeToClient.flush();
        }
    }

    public void updateTime(long milliseconds) {
        this.lastUpdatedTime = milliseconds;
        this.backend.updateLastHeartbeatReceivedTime(milliseconds);

    }

}
