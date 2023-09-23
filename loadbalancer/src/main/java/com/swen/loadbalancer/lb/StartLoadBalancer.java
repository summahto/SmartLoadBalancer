package com.swen.loadbalancer.lb;

import com.swen.loadbalancer.Backends.Backend;

public class StartLoadBalancer {
    public static void main(String[] args) {
        // Start the heartbeat
        Backend backend1 = new Backend(6000);
        Backend backend2 = new Backend(6001);

        Loadbalancer loadbalancer1 = new Loadbalancer(6000);
        Thread loadbalancerThread1 = new Thread(loadbalancer1);

        Loadbalancer loadbalancer2 = new Loadbalancer(6001);
        Thread loadbalancerThread2 = new Thread(loadbalancer2);

        loadbalancerThread1.start();
        loadbalancerThread2.start();

        backend1.start();
        backend2.start();
    }
}
