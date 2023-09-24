package com.swen.loadbalancer.lb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StartLoadBalancer {
    public static void main(String[] args) {

        int numberOfCustomers = 100;
        // Simulating requests coming from clients across the globe
        List<String> customerDataChaseBank = generateCustomerDataList(numberOfCustomers);

        customerDataChaseBank.forEach((customerData) -> System.out.println(customerData));

        BlockingQueue<String> blockingQueue1 = new LinkedBlockingQueue<>();

        addInitialData(customerDataChaseBank, blockingQueue1, 0, 10);

        BlockingQueue<String> blockingQueue2 = new LinkedBlockingQueue<>();
        addInitialData(customerDataChaseBank, blockingQueue2, 10, 20);

        Backend backend1 = new Backend("localhost", "7000", blockingQueue1);
        Backend backend2 = new Backend("localhost", "7001", blockingQueue2);

        // heartbeat Receiver initialization
        HeartBeatReceiver heartBeatReceiver1 = new HeartBeatReceiver(6001, backend1);
        HeartBeatReceiver heartBeatReceiver2 = new HeartBeatReceiver(6002, backend1);

        Thread heartbeatReceiverThread1 = new Thread(heartBeatReceiver1);
        Thread heartbeatReceiverThread2 = new Thread(heartBeatReceiver2);

        // starting heartbeat Receivers
        heartbeatReceiverThread1.start();
        heartbeatReceiverThread2.start();

        // sleeping for 11 seconds so that the heratbeat sender and receiver can connect

        System.out.println("sleeping for 20 seconds so that Heartbeat Sender and HeartBeat receiver can connect");
        try {
            Thread.sleep(21000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Load Balancer initialization
        Loadbalancer loadbalancer1 = new Loadbalancer(7000, backend1);
        Thread loadbalancerThread1 = new Thread(loadbalancer1);

        Loadbalancer loadbalancer2 = new Loadbalancer(7001, backend2);
        Thread loadbalancerThread2 = new Thread(loadbalancer2);

        // Starting Load Balancers
        loadbalancerThread1.start();
        loadbalancerThread2.start();

        ServerPool serverPool = ServerPool.getInstance();
        serverPool.addBackend(backend1);
        serverPool.addBackend(backend2);

        Map<String, Backend> backendMap = serverPool.getBackends();

        int i = 20, j = 0;
        while (i < customerDataChaseBank.size()) {

            Iterator<Map.Entry<String, Backend>> iterator = backendMap.entrySet().iterator();

            while (iterator.hasNext()) {

                Backend b1 = iterator.next().getValue();
                if (b1.isAlive()) {

                    try {

                        b1.getMessageQueue().put(customerDataChaseBank.get(i));
                        i++;

                    } catch (InterruptedException e) {

                        System.err.println("Unable to add data message to the queue");
                        e.printStackTrace();
                    }
                } else {
                    System.err
                            .println("****** Backend running on  port " + b1.getPort() + " has stopped. ********");
                    System.out.println("Routing requests to other servers");

                    iterator.remove();
                    // if (j == 1) {
                    // System.out.println("Exiting now..");
                    // System.exit(0);

                    // }
                }

            }

        }

    }

    private static void addInitialData(List<String> customerDataChaseBank, BlockingQueue<String> blockingQueue1,
            int start, int end) {
        for (int i = start; i < end; i++) {

            try {
                blockingQueue1.put(customerDataChaseBank.get(i));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static List<String> generateCustomerDataList(int numberOfCustomers) {
        return IntStream.range(0, numberOfCustomers)
                .mapToObj(index -> generateCustomerData(index))
                .collect(Collectors.toList());
    }

    public static String generateCustomerData(int customerId) {
        // Simulate generating customer data as a string
        String customerData = "Customer ID: " + customerId +
                ", Account Number: Chase" + (customerId + (int) (Math.random() * 100000)) +
                ", Balance: $" + (Math.random() * 10000);

        return customerData;
    }
}
