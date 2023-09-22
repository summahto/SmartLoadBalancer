package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Backend {

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
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start(Socket server){
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    private void sendHeartbeat() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        
        try {
            // Create a ServerSocket and bind it to the specified port
            serverSocket = new ServerSocket(port);
            System.out.println("Listening for heartbeat on port " + port);

            // Accept a client connection (the Load Balancer)
            clientSocket = serverSocket.accept();

            // send dummy message to load balancer
            System.out.println("Received heartbeat request from " + port);

            // Assuming the heartbeat is successful
            isAlive.set(true);

            // Simulate receiving a request from the LoadBalancer
            // Receive the Load Balancer request here
            receiveRequest(clientSocket);
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

    private void receiveRequest(Socket clientSocket) throws IOException {
        // BufferedReader and read the request from the input stream of the clientSocket
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String request = reader.readLine();
        System.out.println("Received request from LoadBalancer: " + request);
    }

    public static void main(String[] args){
        // // Start the heartbeat
        // Backend backend1 = new Backend(6000);  
        // Backend backend2 = new Backend(6001);  
                
        // backend1.start();
        // backend2.start();
        
    }
}