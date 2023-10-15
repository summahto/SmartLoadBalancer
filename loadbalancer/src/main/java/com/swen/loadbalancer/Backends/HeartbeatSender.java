package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatSender implements Runnable {

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

    @Override
    public void run() {
        start();
    }

    public void start() {
        try {
            Random random = new Random();
            randomStopCount = random.nextInt(10, 15);
            System.out.println("Random stop count to stop the heartbeat : " + randomStopCount);
            while (getIsConnectedFromHeartBeatReceiver() == true) {
                System.out.println("Heartbeat reciever is busy \n");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
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
            if (server != null && !server.isClosed()) {
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

    public Boolean getIsConnectedFromHeartBeatReceiver() {
        try {
            // Define the URL of the HeartBeatReceiver's /connection endpoint
            String url = "http://localhost:8001/connection"; // Adjust the URL as needed

            // Create a URL object
            URL apiUrl = new URL(url);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the HTTP request method to GET
            connection.setRequestMethod("GET");

            // Get the response code (200 indicates success)
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Read the response content
                try (InputStreamReader in = new InputStreamReader(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(in)) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Parse the response content to get the last updated time
                    return Boolean.parseBoolean(response.toString());
                }
            } else {
                // Handle the case where the request was not successful
                System.err.println("Failed to send heartbeats. Status code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Return a default value or handle the error case accordingly
    }

}
