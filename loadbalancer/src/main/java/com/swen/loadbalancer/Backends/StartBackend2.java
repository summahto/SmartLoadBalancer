package com.swen.loadbalancer.Backends;

public class StartBackend2 {

    public static void main(String[] args) {

        Backend backend2 = new Backend(6001);
        backend2.start();

    }

}
