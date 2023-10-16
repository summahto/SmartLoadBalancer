package com.swen.loadbalancer.Backends;

public class StartBackend1 {

    public static void main(String[] args) {
        HeartbeatSender heartbeatSender2 = new HeartbeatSender(6001, 7001);
        BackendRunnable backendRunnable2 = new BackendRunnable(7001, 900, heartbeatSender2);

        // sending heartbeat to loadbalancer listening on port 6000
        Thread backendHeartbeatSender2 = new Thread(heartbeatSender2);
        backendHeartbeatSender2.start();

        Thread backend2 = new Thread(backendRunnable2);
        backend2.start();

    }

}
