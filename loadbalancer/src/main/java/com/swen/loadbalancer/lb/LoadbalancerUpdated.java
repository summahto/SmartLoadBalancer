package com.swen.loadbalancer.lb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;

public class LoadbalancerUpdated implements Runnable {

    private static final long MAX_WAIT_TIME_IN_MILLISECONDS = 20000;

    private int olderActivePort;

    private int currentActiveServerPort = 0;

    public LoadbalancerUpdated(int port) {
        this.olderActivePort = port;
    }

    @Override
    public void run() {

        while (this.olderActivePort != this.currentActiveServerPort) {
            getLastUpdatedTimeFromHeartBeatReceiver();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Attempting to connect to active server port ...");
        }
        this.olderActivePort = this.currentActiveServerPort;

        connectToServer();
    }

    private void connectToServer() {
        try (Socket server = new Socket("localhost", this.olderActivePort);
                OutputStream toBackend = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toBackend);
                PrintWriter writeToBackend = new PrintWriter(outputStreamWriter)) {

            System.out.println("Connected to server port " + this.olderActivePort);
            System.out.println("waiting for a few seconds for heart beat receiver to start");
            try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Make an HTTP request to the HeartBeatReceiver to get the last updated time
            long lastUpdatedTime = 0;
            String msg;
            int idx = 0;
            do {
                lastUpdatedTime = getLastUpdatedTimeFromHeartBeatReceiver();
                msg = "data : " + idx;

                if (idx++ % 100000 == 0) {
                    System.out
                            .println(
                                    "Message sent to server running on port : " + this.olderActivePort + " : " + msg);
                    writeToBackend.println(msg);
                    writeToBackend.flush();

                }
                // System.out.println("Sending data to backend");
            } while ((System.currentTimeMillis() - lastUpdatedTime <= MAX_WAIT_TIME_IN_MILLISECONDS)
                    && (this.olderActivePort == this.currentActiveServerPort));

            System.out.println("Server is down.");
            System.out.println("Connecting to a different backend server");
            // 7001 -------------------- 9000
            getLastUpdatedTimeFromHeartBeatReceiver();
            this.olderActivePort = this.currentActiveServerPort;

            connectToServer();

            throw new ServerNotActiveException("No heartbeat found. Server is not available.");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ServerNotActiveException sne) {
            System.out.println(sne.getMessage());
            sne.printStackTrace();
        }
    }

    private long getLastUpdatedTimeFromHeartBeatReceiver() {
        try {
            // Define the URL of the HeartBeatReceiver's /heartbeat endpoint
            String url = "http://localhost:8001/heartbeat"; // Adjust the URL as needed

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

                    // Parse the http reponse into variables. Format Time:ActivePort
                    String r = response.toString();
                    String[] responses = r.split(":");
                    this.currentActiveServerPort = Integer.parseInt(responses[1]);

                    // Parse the response content to get the last updated time
                    return Long.parseLong(responses[0].toString());
                }
            } else {
                // Handle the case where the request was not successful
                System.err.println("Failed to retrieve last updated time. Status code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1; // Return a default value or handle the error case accordingly
    }

    public static void main(String[] args) {
        LoadbalancerUpdated loadbalancer = new LoadbalancerUpdated(7001);
        Thread lbThread = new Thread(loadbalancer);

        lbThread.start();
    }
}
