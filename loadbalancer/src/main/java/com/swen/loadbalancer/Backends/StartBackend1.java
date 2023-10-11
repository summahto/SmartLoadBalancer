package com.swen.loadbalancer.Backends;

public class StartBackend1 {

    public static void main(String[] args) {

        // Start the heartbeat

        HeartbeatSender heartbeatSender2 = new HeartbeatSender(6001, 7001);
        BackendRunnable backendRunnable2 = new BackendRunnable(7001, heartbeatSender2);

        // sending heartbeat to loadbalancer listening on port 6000
        backendRunnable2.getHeartbeatSender().start();

        Thread backend2 = new Thread(backendRunnable2);
        backend2.start();

    }

}
