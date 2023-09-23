package com.swen.loadbalancer.Backends;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Backend1 {
    public int SENDING_INTERVAL_SECONDS = 10;
    private ScheduledExecutorService heartbeatScheduler;
    private int listeningPort;
    private AtomicBoolean isAlive;

    public Backend1(int listeningPort) {
        this.listeningPort = listeningPort;
        this.isAlive = new AtomicBoolean(false);
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);
        // new Thread(() -> receiveHeartbeat(listeningPort)).start();
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    private void sendHeartbeat() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();

            // Heartbeat message to send
            String heartbeatMessage = "Heartbeat";
            byte[] data = heartbeatMessage.getBytes();

            InetAddress address = InetAddress.getByName("localhost"); // Change to the destination IP address
            DatagramPacket packet = new DatagramPacket(data, data.length, address, listeningPort);

            // Send the heartbeat packet
            datagramSocket.send(packet);
            System.out.println("Heartbeat message sent to port " + listeningPort);

            // Assuming the heartbeat is successful
            isAlive.set(true);
        } catch (IOException e) {
            // Handle exceptions, e.g., connection errors
            System.err.println("Error sending heartbeat on port " + listeningPort);
            isAlive.set(false);
        } finally {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
            }
        }
    }

    private void receiveHeartbeat(int targetPort) {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(targetPort);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);

                // Process the received message
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message from port " + packet.getPort() + ": " + receivedMessage);
            }
        } catch (IOException e) {
            // Handle exceptions, e.g., socket closure
            e.printStackTrace();
        } finally {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        int listeningPort = 6000;

        Backend1 backend1 = new Backend1(listeningPort);
        backend1.start();

        String hostname;
        if (args.length == 1)
            hostname = args[0];
        else
            hostname = "localhost";

        try (Socket server = new Socket(hostname, 6000);
                InputStream fromServer = server.getInputStream();
                InputStreamReader reader = new InputStreamReader(fromServer);
                BufferedReader brFromServer = new BufferedReader(reader);

                OutputStream toServer = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toServer);
                PrintWriter serverWriter = new PrintWriter(outputStreamWriter)) {

            System.out.println("This is a guessing game, you have to guess a number between 1-100 in 6 attempts");
            System.out.println("Commands to play the game \n1. guess [number] \n2. restart \n3. quit");
            System.out.println("enter your guesses [press enter] :");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            String line;
            do {
                serverWriter.println(input);
                serverWriter.flush();
                line = brFromServer.readLine();
                System.out.println(line);

                if (line.equalsIgnoreCase("correct")) {

                    System.out.println("You won. Congratulations.");
                    System.out.println("To play again enter restart [press enter]");
                    System.out.println("To quit playing enter quit [press enter]");
                    input = scanner.nextLine();

                } else if (line.equalsIgnoreCase("game_over")) {

                    System.out.println("Ending game...");
                    System.exit(1);
                    scanner.close();

                } else {

                    System.out.println("Try again : ");
                    input = scanner.nextLine();
                }

            } while (line != null);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
}