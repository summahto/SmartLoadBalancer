# SmartLoadBalancer

This is a Load Balancer which is tracking the health of all the servers available (through a hearthbeat mechanism) and routes requests to a different server based on its availability.
The Smart Load Balancer is a robust two-way communication system that employs a "heartbeat tactic" mechanism to optimize server traffic distribution.

# Overview

In essence, a load balancer serves as a reverse proxy, efficiently distributing communication traffic among multiple servers. This not only enhances system performance but also reduces the overall server maintenance burden.

# Getting Started

To run and test the Smart Load Balancer, clone the repo and open the project and open it in an IDE. After that, follow these steps:

# 1 Start the backend 
Execute StartBackend1.java by running the main function. This will trigger a response from the load balancer, confirming the successful establishment of connections.

# 2 Run the HeartBeatReciever file
The HeartBeatRecieverUpdated.java file is the next class to run via the main function. This will wait for heartbeats sent by the backend server.

# 3 Launch the loadbalancer:
Run the main function of LoadBalancerUpdated.java : This action initiates the load balancer, causing it to await connections from the backend servers.

Heartbeat Mechanism
As the servers operate, they continuously send heartbeats to the load balancer at regular intervals. This mechanism ensures constant monitoring between the server(s) and the load balancer.

# Handling Failures

In the event of a non-deterministic failure within the server-heartbeat process, the server may crash. This crash is triggered by a randomInt value that is generated at the begining. In such cases, an error message will be generated and the backend will terminate. The loadbalancer will remain running as it awaits for any server connections.

Feel free to explore the Smart Load Balancer and utilize its features to enhance the performance and reliability of your system.

# Architecture diagram - Lucid

https://lucid.app/lucidchart/aa098156-b1a4-4d1c-92af-6f8a3f7f9e73/edit?viewport_loc=-1096%2C-1028%2C2832%2C1556%2C0_0&invitationId=inv_2b792545-3d28-445f-8a80-425cad4dc68a
