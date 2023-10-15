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

    private int port;

    private int activeServerPort = 0;

    public LoadbalancerUpdated(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        getLastUpdatedTimeFromHeartBeatReceiver();

        while (this.port != this.activeServerPort){
            System.out.println("Attempting to connect to active server port ...");
        }
        
        try (Socket server = new Socket("localhost", this.activeServerPort);
                OutputStream toBackend = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toBackend);
                PrintWriter writeToBackend = new PrintWriter(outputStreamWriter)) {
            
            System.out.println("Connected to server port " + this.activeServerPort);
            System.out.println("waiting for a few seconds for heart beat receiver to start");
            try {
                Thread.sleep(20000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Make an HTTP request to the HeartBeatReceiver to get the last updated time
            long lastUpdatedTime = 0;
            do {
                lastUpdatedTime = getLastUpdatedTimeFromHeartBeatReceiver();
                System.out.println("Sending data to backend");
            } while ((System.currentTimeMillis() - lastUpdatedTime <= MAX_WAIT_TIME_IN_MILLISECONDS));

            System.out.println("Server is down.");
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
                    this.activeServerPort = Integer.parseInt(responses[1]);
                    
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
