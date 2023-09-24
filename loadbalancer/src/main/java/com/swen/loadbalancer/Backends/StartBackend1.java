package com.swen.loadbalancer.Backends;

public class StartBackend1 {

    public static void main(String[] args) {
        // Start the heartbeat

        HeartbeatSender heartbeatSender = new HeartbeatSender(6001);
        BackendRunnable backendRunnable1 = new BackendRunnable(7000, heartbeatSender);

        // sending heartbeat to loadbalancer listening on port 6000
        backendRunnable1.getHeartbeatSender().start();

        // start backend which listens on port number 7000
        Thread backend1 = new Thread(backendRunnable1);
        backend1.start();

    }

}
