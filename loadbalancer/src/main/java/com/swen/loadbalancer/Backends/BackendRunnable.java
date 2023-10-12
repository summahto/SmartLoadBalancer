package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BackendRunnable implements Runnable {

    private static int count = 0;

    private int port;
    private HeartbeatSender heartbeatSender;

    public BackendRunnable(int port, HeartbeatSender heartbeatSender) {
        this.port = port;
        this.heartbeatSender = heartbeatSender;
    }

    public HeartbeatSender getHeartbeatSender() {
        return heartbeatSender;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port);
                // server socket is created and awaits an incoming connection using
                Socket socket = serverSocket.accept();
                InputStream fromLB = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromLB);
                BufferedReader brFromLB = new BufferedReader(reader);) {

            // Read in the data sent from the loadbalancer
            String request = "Hi";

            do {
                request = brFromLB.readLine();
                System.out.println("Received request from LoadBalancer: " + request);

            } while (request != null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}