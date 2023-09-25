package com.swen.loadbalancer.lb;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HeartBeatReceiverUpdated implements Runnable {

    private long lastUpdatedTime = 0;

    public HeartBeatReceiverUpdated() {
    }

    @Override
    public void run() {
        // Create a separate thread to listen for heartbeats on port 6000
        Thread heartbeatThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(6001)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    handleHeartbeat(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
        heartbeatThread.start();

        // Create an HTTP server to expose the /heartbeat endpoint
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

            // Create a context for the /heartbeat endpoint
            server.createContext("/heartbeat", new HeartbeatHandler());

            // Start the HTTP server
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handleHeartbeat(Socket socket) {
        try (InputStream fromClient = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromClient);
                BufferedReader brFromClient = new BufferedReader(reader);) {

            String line = null;
            String responseToClient = null;

            int value = 0;

            // while ((line = brFromClient.readLine()) != null)
            while (true) {

                line = brFromClient.readLine();
                String[] tokens = line.split(" ");

                String command;
                if (tokens.length == 1)
                    command = tokens[0];
                else if (tokens.length == 2) {
                    command = tokens[0];
                    value = Integer.valueOf(tokens[1]);
                } else {
                    command = "invalid";
                }

                switch (command) {

                    case "heartbeat":
                        this.lastUpdatedTime = System.currentTimeMillis();
                        responseToClient = "Received your heartbeat, updating your status";
                        break;

                    default:
                        responseToClient = "error";
                        break;
                }

                System.out.println(responseToClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define an HTTP handler for the /heartbeat endpoint
    class HeartbeatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Respond with the last heartbeat update time
            String response = String.valueOf(lastUpdatedTime);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) {
        HeartBeatReceiverUpdated heartBeatReceiver = new HeartBeatReceiverUpdated();
        Thread heartBeatReceiverThread = new Thread(heartBeatReceiver);

        heartBeatReceiverThread.start();
    }

}
