package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BackendRunnable implements Runnable {

    private static int count = 0;

    // public int SENDING_INTERVAL_SECONDS = 30;
    // private ScheduledExecutorService heartbeatScheduler;
    private int port;
    private HeartbeatSender heartbeatSender;

    public BackendRunnable(int port, HeartbeatSender heartbeatSender) {
        this.port = port;
        this.heartbeatSender = heartbeatSender;
        // this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public HeartbeatSender getHeartbeatSender() {
        return heartbeatSender;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port);
                // server socket is created and awaits an incoming connection using
                // serverSocket.accept()
                Socket socket = serverSocket.accept();
                InputStream fromLB = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromLB);
                BufferedReader brFromLB = new BufferedReader(reader);) {

            // Method to get heartbeat from the backends
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

// public void start() {
// try {
// server = new Socket("localhost", this.port);
// isAlive.set(true); // Mark the backend as alive when the connection is
// established
// } catch (IOException e) {
// // Handle connection errors
// System.err.println("Error establishing the initial connection on port " +
// port);
// isAlive.set(false);
// }

// // scheduling the heartbeat with an interval
// heartbeatScheduler.scheduleAtFixedRate(() -> sendHeartbeat(), 0,
// SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);

// // Getting requests from the loadbalancer (this.port)
// receiveRequest();

// }
