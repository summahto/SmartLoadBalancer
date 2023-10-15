package com.swen.loadbalancer.Backends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class BackendRunnable implements Runnable {

    private static int count = 0;

    private int serverPort;
    private int otherServerPort;
    private HeartbeatSender heartbeatSender;
    private boolean connectedToClient = false; // Flag to track client connection

    private static final String KAFKA_TOPIC = "backend-messages"; // Replace with your Kafka topic name
    private final Producer<String, String> kafkaProducer;
    private final Consumer<String, String> kafkaConsumer;

    public BackendRunnable(int port, int otherServerPort, HeartbeatSender heartbeatSender) {
        this.serverPort = port;
        this.heartbeatSender = heartbeatSender;
        this.otherServerPort = otherServerPort;

        // Kafka producer configuration
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka broker
                                                                                      // address
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<>(producerProps);

        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka broker
                                                                                      // address
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "backend-consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<>(consumerProps);
        kafkaConsumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
    }

    public HeartbeatSender getHeartbeatSender() {
        return heartbeatSender;
    }

    @Override
    public void run() {
        // Create a separate thread for the server socket of Backend 2
        Thread serverThread = new Thread(() -> {
            startServer();
        });

        serverThread.start();

        System.out.println("Waiting for server to accept connection.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Start subscription only if Backend 2 is not connected to the client
        if (!connectedToClient) {
            startSubscription();
        }
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        InputStream fromLB = socket.getInputStream();
                        InputStreamReader reader = new InputStreamReader(fromLB);
                        BufferedReader brFromLB = new BufferedReader(reader)) {

                    // Set the connectedToClient flag to true when a client connection is
                    // established
                    connectedToClient = true;

                    // Read in the data sent from the load balancer
                    String request;
                    while ((request = brFromLB.readLine()) != null) {
                        System.out.println("Received request from LoadBalancer: " + request);
                        // send data to other backend available
                        kafkaProducer.send(new ProducerRecord<String, String>(KAFKA_TOPIC, request));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Set the connectedToClient flag to false when the client connection is lost
                    connectedToClient = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread heartbeatSendeThread = new Thread(this.heartbeatSender);
        heartbeatSendeThread.start();
        // starting backend again
        run();

    }

    private void startSubscription() {
        while (!connectedToClient) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500));

            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                // Handle the received message from Backend (2)
                System.out.println("Received message from other Backend: " + message);
            }
        }
    }

    // @Override
    // public void run() {
    // try (ServerSocket serverSocket = new ServerSocket(this.port);
    // // server socket is created and awaits an incoming connection using
    // Socket socket = serverSocket.accept();
    // InputStream fromLB = socket.getInputStream();
    // InputStreamReader reader = new InputStreamReader(fromLB);
    // BufferedReader brFromLB = new BufferedReader(reader);) {

    // // Read in the data sent from the loadbalancer
    // String request = "Hi";

    // do {
    // request = brFromLB.readLine();
    // System.out.println("Received request from LoadBalancer: " + request);

    // } while (request != null);

    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // }

}