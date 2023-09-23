package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Backend {

    private static int count = 0;

    public int SENDING_INTERVAL_SECONDS = 5;
    private ScheduledExecutorService heartbeatScheduler;
    private int port;
    private AtomicBoolean isAlive;
    private Socket server;

    public Backend(int port) {
        this.port = port;
        this.isAlive = new AtomicBoolean(false);
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    public void start() {
        try {
            server = new Socket("localhost", this.port);
            isAlive.set(true); // Mark the backend as alive when the connection is established
        } catch (IOException e) {
            // Handle connection errors
            System.err.println("Error establishing the initial connection on port " + port);
            isAlive.set(false);
        }

        // scheduling the heartbeat with an interval
        heartbeatScheduler.scheduleAtFixedRate(() -> sendHeartbeat(), 0, SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void sendHeartbeat() {
        try {
            if (server != null && !server.isClosed()) {
                OutputStream toServer = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
                PrintWriter serverWriter = new PrintWriter(outputStreamWriter);

                // Send Heartbeat
                System.out.println("Sending heartbeat for " + count++ + " from backend to load-balancer");
                serverWriter.println("heartbeat");
                serverWriter.flush();
            } else {
                // Handle case where the connection is closed
                System.err.println("Connection on port " + port + " is closed. Re-establishing...");
                server = new Socket("localhost", this.port);
            }
        } catch (IOException e) {
            // Handle other exceptions
            System.err.println("Error sending heartbeat on port " + port);
            isAlive.set(false);
        }
    }

    // private void sendHeartbeat() {

    // try (Socket server = new Socket("localhost", this.port);
    // // InputStream fromServer = server.getInputStream();
    // // InputStreamReader reader = new InputStreamReader(fromServer);
    // // BufferedReader brFromServer = new BufferedReader(reader);

    // OutputStream toServer = server.getOutputStream();
    // OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
    // PrintWriter serverWriter = new PrintWriter(outputStreamWriter)) {

    // // Send Hearbeat
    // System.out.println("Sending heartbeat for " + count++ + " from backend to
    // load-balancer");
    // serverWriter.println("heartbeat");
    // serverWriter.flush();

    // // String line;
    // // do {
    // // line = brFromServer.readLine();

    // // if (!line.contains("heartbeat")) {
    // // receiveRequest(line);

    // // }

    // // } while (line != null);

    // } catch (IOException e) {
    // // Handle exceptions, e.g., connection errors
    // e.printStackTrace();
    // System.err.println("Error sending heartbeat on port " + port);
    // }
    // }

    private void receiveRequest(String request) throws IOException {
        // BufferedReader and read the request from the input stream of the clientSocket
        // BufferedReader reader = new BufferedReader(new
        // InputStreamReader(clientSocket.getInputStream()));
        // String request = reader.readLine();
        System.out.println("Received request from LoadBalancer: " + request);

        // Process the request as needed
    }
}