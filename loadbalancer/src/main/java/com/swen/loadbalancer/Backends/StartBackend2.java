package com.swen.loadbalancer.Backends;

public class StartBackend2 {

    public static void main(String[] args) {
        HeartbeatSender heartbeatSender1 = new HeartbeatSender(6001, 9000);
        BackendRunnable backendRunnable1 = new BackendRunnable(9000, 7001, heartbeatSender1);

        // sending heartbeat to loadbalancer listening on port 6000
        Thread backendHeartbeatSender = new Thread(heartbeatSender1);
        backendHeartbeatSender.start();
        // start backend which listens on port number 7000
        Thread backend1 = new Thread(backendRunnable1);
        backend1.start();

    }

}