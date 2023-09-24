package com.swen.loadbalancer.lb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Loadbalancer implements Runnable {

    // TODO : When LB and Backends are in different machines, host info needs to be
    // stored.
    // private int host;
    private int port;
    private Backend backend;
    // private BlockingQueue<String> blockingQueue;

    public Loadbalancer(int port, Backend backend) {
        this.port = port;
        this.backend = backend;
    }

    @Override
    public void run() {

        try (Socket server = new Socket("localhost", this.port);

                OutputStream toBackend = server.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(toBackend);
                PrintWriter writeToBackend = new PrintWriter(outputStreamWriter)) {

            System.out.println("Connection established.. To Backend running on port : " + this.port);

            System.out.println("sleeping for 5 seconds to get data from customers");
            try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean retry = false;
            for (int i = 0; i < 3; i++) {

                retry = sendDataToBackend(writeToBackend);
                if (retry == false)
                    break;
                else {
                    System.out.println("retrying once more");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean sendDataToBackend(PrintWriter writeToClient) throws InterruptedException {

        boolean retry = true;
        while (!this.backend.getMessageQueue().isEmpty()) {
            System.out.println("Sending data to server running on port: " + this.backend.getPort());
            writeToClient.println(this.backend.getMessageQueue().take());
            writeToClient.flush();

            retry = false;
        }
        return retry;
    }

    // private void getHeartBeat(BufferedReader brFromClient) throws IOException {

    // String line = null;
    // String responseToClient = null;

    // Random random1 = new Random();
    // int originalAnswer = random1.nextInt(100);
    // int guessValue = 0;
    // int guessAttempt = 0;

    // while ((line = brFromClient.readLine()) != null) {
    // String[] tokens = line.split(" ");

    // String command;
    // if (tokens.length == 1)
    // command = tokens[0];
    // else if (tokens.length == 2) {
    // command = tokens[0];
    // guessValue = Integer.valueOf(tokens[1]);
    // } else {
    // command = "invalid";
    // }

    // switch (command) {
    // case "restart": {
    // Random random2 = new Random();
    // originalAnswer = random2.nextInt(100);
    // guessAttempt = 0;
    // responseToClient = "restarted";
    // System.out.println("game restarted");
    // }
    // break;

    // case "guess": {
    // guessAttempt++;

    // if (guessAttempt > 6) {
    // responseToClient = "out_of_guesses";

    // } else {
    // if (guessValue < originalAnswer) {
    // responseToClient = "toolow";
    // } else if (guessValue > originalAnswer) {
    // responseToClient = "toohigh";
    // } else if (guessValue == originalAnswer) {
    // responseToClient = "correct";
    // }
    // }
    // }
    // break;

    // case "quit":
    // responseToClient = "game_over";
    // break;

    // case "heartbeat":
    // updateTime(System.currentTimeMillis());
    // responseToClient = "Received your heartbeat, updating your status";
    // break;

    // default:
    // responseToClient = "error";
    // break;
    // }

    // System.out.println(responseToClient);
    // // writeToClient.println(responseToClient);
    // // writeToClient.flush();
    // }
    // }

    // @Override
    // public void updateTime(long milliseconds) {
    // this.lastUpdatedTime = milliseconds;
    // this.backend.updateLastHeartbeatReceivedTime(milliseconds);

    // }

    // @Override
    // public boolean checkAlive() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'checkAlive'");
    // }

}
