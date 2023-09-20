package com.swen.loadbalancer;

public class StartLoadBalancer {
    public static void main(String[] args) {

        LoadbalancerRunnable loadbalancerRunnable1 = new LoadbalancerRunnable(6000);
        Thread loadbalancerThread1 = new Thread(loadbalancerRunnable1);

        LoadbalancerRunnable loadbalancerRunnable2 = new LoadbalancerRunnable(6001);
        Thread loadbalancerThread2 = new Thread(loadbalancerRunnable2);

        loadbalancerThread1.start();
        loadbalancerThread2.start();

    }
}
