# SmartLoadBalancer

This is a Load Balancer which is tracking the health of all the servers available (through a hearthbeat mechanism) and routes requests to a different server based on its availability
The Smart Load Balancer is a robust two-way communication system that employs a "heartbeat tactic" mechanism to optimize server traffic distribution.

# Overview

In essence, a load balancer serves as a reverse proxy, efficiently distributing communication traffic among multiple servers. This not only enhances system performance but also reduces the overall server maintenance burden.

# Getting Started

To run and test the Smart Load Balancer, follow these steps:

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

Passive Redundancy:
We use Kafka as the checkpoint for the backend servers to communicate the data incase of any server failures. The loadbalancer is continuously updated with the most active server that the heartbeatreciever is communicating with.

# Handling Failures

In the event of a non-deterministic failure within the server-heartbeat process, the server may crash. In such cases, an error message will be generated and displayed to notify the system user, facilitating prompt action.

Feel free to explore the Smart Load Balancer and utilize its features to enhance the performance and reliability of your system.

# Installing Kafka (Windows)
Download and Install Apache Kafka: https://kafka.apache.org/downloads

Run these commands in separate terminals, sequentially, under the kafka folder you extracted:

CMD Terminal 1 (Start the zookeeper service)

.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

CMD Terminal 2 (Start the Kafka server)

.\bin\windows\kafka-server-start.bat .\config\server.properties


# Installing Kafka (Mac)

Download (https://www.apache.org/dyn/closer.cgi?path=/kafka/3.6.0/kafka_2.13-3.6.0.tgz) the latest Kafka release and extract it:

$ tar -xzf kafka_2.13-3.6.0.tgz
$ cd kafka_2.13-3.6.0

start kafka

Open a terminal and run the following commands one by one. 

$ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

$ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties

$ bin/kafka-server-start.sh config/kraft/server.properties

You should see kafka running on your terminal.

# Diagram:

Passive Redundancy Functionality Diagram - Lucid

https://lucid.app/lucidchart/aa098156-b1a4-4d1c-92af-6f8a3f7f9e73/edit?viewport_loc=-1096%2C-1028%2C2832%2C1556%2C0_0&invitationId=inv_2b792545-3d28-445f-8a80-425cad4dc68a
