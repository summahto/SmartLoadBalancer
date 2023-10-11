package com.swen.loadbalancer.Backends;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatSender {

    private static int count = 0;

    public int SENDING_INTERVAL_SECONDS = 10;
    private int hbReceiverPort;
    private int backendServerPort;
    private ScheduledExecutorService heartbeatScheduler;
    private Socket server;
    private int randomStopCount;

    public HeartbeatSender(int hbReceiverPort, int backendServerPort) {
        this.hbReceiverPort = hbReceiverPort;
        this.backendServerPort = backendServerPort;
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        try {
            Random random = new Random();
            randomStopCount = random.nextInt(1, 11);
            System.out.println("Random stop count to stop the heartbeat : " + randomStopCount);
            server = new Socket("localhost", this.hbReceiverPort);
        } catch (IOException e) {
            // Handle connection errors
            System.err.println("Error establishing the initial connection on port " + hbReceiverPort);
        }

        // scheduling the heartbeat with an interval
        heartbeatScheduler.scheduleAtFixedRate(() -> sendHeartbeat(), 0, SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);

    }

    private void sendHeartbeat() {
        try {
            if (server != null && !server.isClosed() && server.isConnected()) {
                OutputStream toServer = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
                PrintWriter serverWriter = new PrintWriter(outputStreamWriter);

                // Send Heartbeat
                System.out.println("Sending heartbeat " + count++ + " for from backend to load-balancer");

                if (count == randomStopCount) {
                    System.exit(1);
                }

                serverWriter.println("heartbeat:" + this.backendServerPort);
                serverWriter.flush();
            } else {
                // Handle case where the connection is closed
                System.err.println("Connection on port " + hbReceiverPort + " is closed. Re-establishing...");
                server = new Socket("localhost", this.hbReceiverPort);
            }
        } catch (IOException e) {
            // Handle other exceptions
            System.err.println("Error sending heartbeat on port " + hbReceiverPort);
        }
    }

}
