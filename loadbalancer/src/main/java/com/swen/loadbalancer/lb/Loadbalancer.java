package com.swen.loadbalancer.lb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.server.ServerNotActiveException;

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

            while (backend.isAlive())
                sendDataToBackend(writeToBackend);

            throw new ServerNotActiveException(
                    " Backend running at port : " + this.backend.getPort() + " is not available");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ServerNotActiveException sne) {
            System.out.println(sne.getMessage());
            sne.printStackTrace();
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
}
