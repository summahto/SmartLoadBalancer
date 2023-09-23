package com.swen.loadbalancer.Backends;

public class StartBackend1 {

    public static void main(String[] args) {
        // Start the heartbeat
        Backend backend1 = new Backend(6000);
        backend1.start();

    }

}
