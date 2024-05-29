# SmartLoadBalancer

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
5. [Heartbeat Mechanism](#heartbeat-mechanism)
6. [Handling Failures](#handling-failures)
7. [Diagrams](#diagrams)
8. [Contributing](#contributing)
9. [License](#license)

## Overview

The SmartLoadBalancer is a robust load balancing system that optimizes server traffic distribution through a two-way communication mechanism known as the "heartbeat tactic." It tracks the health of all available servers and routes requests based on server availability, ensuring enhanced system performance and reduced maintenance burdens.

## Features

- **Health Monitoring:** Continuously tracks the health of backend servers.
- **Dynamic Routing:** Routes requests to available servers based on real-time status.
- **Passive Redundancy:** Uses Kafka for checkpointing and data communication to handle server failures.
- **Failure Handling:** Detects and manages server crashes efficiently.

## Architecture

The SmartLoadBalancer operates as a reverse proxy, distributing communication traffic among multiple servers. This not only boosts system performance but also alleviates server maintenance.
![Smart Load Balancer](https://github.com/summahto/SmartLoadBalancer/assets/114707851/4e1fa7b3-2e68-41f2-80f9-d6b18ea0bd75)

## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- Apache Kafka
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

### Installation

#### Installing Kafka (Windows)

1. **Download and Install Apache Kafka:**
   [Apache Kafka Downloads](https://kafka.apache.org/downloads)

2. **Start the Zookeeper Service:**
    ```bash
    .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
    ```

3. **Start the Kafka Server:**
    ```bash
    .\bin\windows\kafka-server-start.bat .\config\server.properties
    ```

#### Installing Kafka (Mac/Linux)

1. **Download and Extract Kafka:**
    ```bash
    $ tar -xzf kafka_2.13-3.6.0.tgz
    $ cd kafka_2.13-3.6.0
    ```

2. **Start Kafka:**
    ```bash
    $ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
    $ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties
    $ bin/kafka-server-start.sh config/kraft/server.properties
    ```

### Running the Application

1. **Start the HeartBeatReceiver:**
    ```java
    Execute the main function in `HeartBeatReceiverUpdated.java`.
    ```

2. **Launch Backend Server 1:**
    ```java
    Run `StartBackend1.java`.
    ```

3. **Start the Load Balancer:**
    ```java
    Execute the main function in `LoadBalancerUpdated.java`.
    ```

4. **Launch Backend Server 2:**
    ```java
    Run `StartBackend2.java`.
    ```

## Heartbeat Mechanism

As the servers operate, they send heartbeats to the load balancer at regular intervals. This ensures continuous monitoring and synchronization between the servers and the load balancer.

![Heartbeat Mechanism Diagram](path_to_heartbeat_mechanism_diagram_image)

## Handling Failures

In the event of a server failure, triggered by a random integer value, the server may crash and an error message will be generated. The backend will terminate, but the load balancer will continue running, awaiting new server connections.

The load balancer uses Kafka for checkpointing to manage data and ensure the system remains operational with the most active servers.


# Diagram:

Passive Redundancy Functionality Diagram - Lucid
=======



