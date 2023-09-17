package com.swen.loadbalancer;

import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServerPool serverPool = new ServerPool();

        // Parse the server list from command line arguments or user input
        if (args.length == 0) {
            System.out.print("Enter one or more backend URLs separated by commas: ");
            Scanner scanner = new Scanner(System.in);
            String serverList = scanner.nextLine();
            args = serverList.split(",");
        }

        // Add backend servers to the server pool
        for (String arg : args) {
            try {
                URL serverURL = new URL(arg.trim());
                serverPool.addBackend(serverURL);
                System.out.println("Configured server: " + serverURL);
            } catch (Exception e) {
                System.err.println("Invalid URL: " + arg);
            }
        }

        // Implement the HTTP server and load balancing logic here
        // Use Apache HttpClient for the HTTP server

        // Start the health check routine in a separate thread
        Thread healthCheckThread = new Thread(() -> {
            while (true) {
                serverPool.healthCheck();
                try {
                    Thread.sleep(120000); // Sleep for 2 minutes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        healthCheckThread.setDaemon(true);
        healthCheckThread.start();

        // Implement the HTTP server and load balancing logic here
    }
}
