package com.swen.loadbalancer.lb;

public class StartLoadBalancer {
    public static void main(String[] args) {

        Loadbalancer loadbalancerRunnable1 = new Loadbalancer(6000);
        Thread loadbalancerThread1 = new Thread(loadbalancerRunnable1);

        Loadbalancer loadbalancerRunnable2 = new Loadbalancer(6001);
        Thread loadbalancerThread2 = new Thread(loadbalancerRunnable2);

        loadbalancerThread1.start();
        loadbalancerThread2.start();

    }
}
