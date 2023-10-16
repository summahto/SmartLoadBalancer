# SmartLoadBalancer

This is a Load Balancer which tracks the health of all the servers available (through a hearthbeat mechanism) and routes requests to a different server based on its availability.
The Smart Load Balancer is a robust two-way communication system that employs a "heartbeat tactic" mechanism to optimize server traffic distribution.

# Overview

In essence, a load balancer serves as a reverse proxy, efficiently distributing communication traffic among multiple servers. This not only enhances system performance but also reduces the overall server maintenance burden.

# Getting Started

To run and test the Smart Load Balancer, clone the repo and open the project and open it in an IDE. After that, follow these steps:

# 1) Start the HeartBeatReciever
Execute the main function of the heartbeatrecieverUpdated.java. This will wait for any heart beats sent by the servers.

# 2) Launch Backend Server 1

Run StartBackend1.java. This will trigger a response from the load balancer, confirming the successful establishment of connections.

# 3) Start the Load Balancer

Execute LoadbalancerUpdated.java by running the main function. This action initiates the load balancer, causing it to await connections from the backend servers.

# 4) Launch Backend Server 1

Run StartBackend2.java. This will trigger a response from the load balancer, confirming the successful establishment of connections.

Heartbeat Mechanism:
As the servers operate, they continuously send heartbeats to the load balancer at regular intervals. This mechanism ensures constant monitoring and synchronization between the servers and the load balancer.
=======
# 1 Start the backend 
Execute StartBackend1.java by running the main function. This will trigger a response from the load balancer, confirming the successful establishment of connections.

# 2 Run the HeartBeatReciever file
The HeartBeatRecieverUpdated.java file is the next class to run via the main function. This will wait for heartbeats sent by the backend server.

# 3 Launch the loadbalancer:
Run the main function of LoadBalancerUpdated.java : This action initiates the load balancer, causing it to await connections from the backend servers.

Heartbeat Mechanism
As the servers operate, they continuously send heartbeats to the load balancer at regular intervals. This mechanism ensures constant monitoring between the server(s) and the load balancer.

Passive Redundancy:
We use Kafka as the checkpoint for the backend servers to communicate the data incase of any server failures. The loadbalancer is continuously updated with the most active server that the heartbeatreciever is communicating with.

# Handling Failures

In the event of a non-deterministic failure within the server-heartbeat process, the server may crash. This crash is triggered by a randomInt value that is generated at the begining. In such cases, an error message will be generated and the backend will terminate. The loadbalancer will remain running as it awaits for any server connections.

Feel free to explore the Smart Load Balancer and utilize its features to enhance the performance and reliability of your system.

# Installing Kafka (Windows)
Download and Install Apache Kafka: https://kafka.apache.org/downloads

Run these commands in separate terminals, sequentially, under the kafka folder you extracted:

CMD Terminal 1 (Start the zookeeper service)

.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

CMD Terminal 2 (Start the Kafka server)

.\bin\windows\kafka-server-start.bat .\config\server.properties


# Installing Kafka (Mac/Linux)

Download (https://www.apache.org/dyn/closer.cgi?path=/kafka/3.6.0/kafka_2.13-3.6.0.tgz) the latest Kafka release and extract it:

$ tar -xzf kafka_2.13-3.6.0.tgz

$ cd kafka_2.13-3.6.0

Starting kafka

Open a terminal and run the following commands one by one. 

$ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

$ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties

$ bin/kafka-server-start.sh config/kraft/server.properties

You should see kafka running on your terminal.

# Diagram:

Passive Redundancy Functionality Diagram - Lucid
=======
# Architecture diagram

![Smart Load Balancer](https://github.com/summahto/SmartLoadBalancer/assets/114707851/4e1fa7b3-2e68-41f2-80f9-d6b18ea0bd75)

