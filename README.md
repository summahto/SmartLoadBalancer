# SmartLoadBalancer
This is a Load Balancer which is tracking the health of all the servers available (through a hearthbeat mechanism) and routes requests to a different server based on its availability
The Smart Load Balancer is a robust two-way communication system that employs a "heartbeat tactic" mechanism to optimize server traffic distribution.

# Overview
In essence, a load balancer serves as a reverse proxy, efficiently distributing communication traffic among multiple servers. This not only enhances system performance but also reduces the overall server maintenance burden.

# Getting Started
To run and test the Smart Load Balancer, follow these steps:

# Start Load Balancer

Execute StartLoadBalancer.java by running the main function. This action initiates the load balancer, causing it to await connections from the backend servers.

Launch Backend Servers

Run both StartBackend1.java and StartBackend2.java. This will trigger a response from the load balancer, confirming the successful establishment of connections.

Heartbeat Mechanism
As the servers operate, they continuously send heartbeats to the load balancer at regular intervals. This mechanism ensures constant monitoring and synchronization between the servers and the load balancer.

# Handling Failures
In the event of a non-deterministic failure within the server-heartbeat process, the server may crash. In such cases, an error message will be generated and displayed to notify the system user, facilitating prompt action.

Feel free to explore the Smart Load Balancer and utilize its features to enhance the performance and reliability of your system.

Diagram:
Lucid Charts: 
