package com.swen.loadbalancer.lb;

import com.swen.loadbalancer.Backends.Backend;

public class StartLoadBalancer {
    public static void main(String[] args) {

        Loadbalancer loadbalancer1 = new Loadbalancer(6000);
        Thread loadbalancerThread1 = new Thread(loadbalancer1);

        Loadbalancer loadbalancer2 = new Loadbalancer(6001);
        Thread loadbalancerThread2 = new Thread(loadbalancer2);

        loadbalancerThread1.start();
        loadbalancerThread2.start();

    }
}
