package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Backend {

    private static int count = 0;
    // Class will implement the heartbeat sender logic here
    // Schedule heart beat to be sent every sending interval

    // TODO : send heartbeat (use ScheduledExecutor)
    // TODO : receive requests from LoadBalancer once the aliveness is verified.

    public int SENDING_INTERVAL_SECONDS = 5;
    private ScheduledExecutorService heartbeatScheduler;
    private int port;
    private AtomicBoolean isAlive;

    public Backend(int port) {
        this.port = port;
        this.isAlive = new AtomicBoolean(false);
        this.heartbeatScheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, SENDING_INTERVAL_SECONDS,
                SENDING_INTERVAL_SECONDS,
                TimeUnit.SECONDS);
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    private void sendHeartbeat() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try (Socket server = new Socket("localhost", 6000);
                InputStream fromServer = server.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromServer);
                BufferedReader brFromServer = new BufferedReader(reader);

                OutputStream toServer = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
                PrintWriter serverWriter = new PrintWriter(outputStreamWriter)) {
            // Create a ServerSocket and bind it to the specified port

            // serverSocket = new ServerSocket(port);
            // System.out.println("Listening for heartbeat on port " + port);

            // Accept a client connection (the Load Balancer)
            // clientSocket = serverSocket.accept();
            // System.out.println("Received heartbeat request from " + port);

            // Assuming the heartbeat is successful
            // isAlive.set(true);

            String line;
            do {
                System.out.println("Sending heartbeat for " + count++ + " from backend to load-balancer");
                serverWriter.println("heartbeat");
                serverWriter.flush();
                line = brFromServer.readLine();

                if (!line.contains("heartbeat")) {
                    receiveRequest(line);

                }

            } while (line != null);

            // Simulate receiving a request from the LoadBalancer
            // Receive the Load Balancer request here
        } catch (IOException e) {
            // Handle exceptions, e.g., connection errors
            System.err.println("Error sending heartbeat on port " + port);
            isAlive.set(false);
        } finally {
            try {
                // Close the client socket
                if (clientSocket != null) {
                    clientSocket.close();
                }
                // Close the server socket
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing sockets: " + e.getMessage());
            }
        }
    }

    private void receiveRequest(String request) throws IOException {
        // BufferedReader and read the request from the input stream of the clientSocket
        // BufferedReader reader = new BufferedReader(new
        // InputStreamReader(clientSocket.getInputStream()));
        // String request = reader.readLine();
        System.out.println("Received request from LoadBalancer: " + request);

        // Process the request as needed
    }
}