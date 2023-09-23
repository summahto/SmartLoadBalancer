package com.swen.loadbalancer.lb;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StartLoadBalancer {
    public static void main(String[] args) {

        int numberOfCustomers = 10;
        // Simulating requests coming from clients across the globe
        List<String> customerDataChaseBank = generateCustomerDataList(numberOfCustomers);

        customerDataChaseBank.forEach((customerData) -> System.out.println(customerData));

        BlockingQueue<String> loadBalBlockingQueue1 = new LinkedBlockingQueue<>();
        BlockingQueue<String> loadBalBlockingQueue2 = new LinkedBlockingQueue<>();

        Loadbalancer loadbalancer1 = new Loadbalancer(6000, loadBalBlockingQueue1);
        Thread loadbalancerThread1 = new Thread(loadbalancer1);

        Loadbalancer loadbalancer2 = new Loadbalancer(6001, loadBalBlockingQueue2);
        Thread loadbalancerThread2 = new Thread(loadbalancer2);

        loadbalancerThread1.start();
        loadbalancerThread2.start();

    }

    public static List<String> generateCustomerDataList(int numberOfCustomers) {
        return IntStream.range(0, numberOfCustomers)
                .mapToObj(index -> generateCustomerData(index))
                .collect(Collectors.toList());
    }

    public static String generateCustomerData(int customerId) {
        // Simulate generating customer data as a string
        String customerData = "Customer ID: " + customerId +
                ", Name: Elon Musk" +
                ", Account Number: Chase" + (customerId + (int) (Math.random() * 100000)) +
                ", Balance: $" + (Math.random() * 10000);

        return customerData;
    }
}
