#!/bin/bash
javac ./lb/HeartBeatReceiverUpdated.java
javac ./Backends/StartBackend2.java
javac ./lb/LoadbalancerUpdated.java

nohup java ./lb/HeartBeatReceiverUpdated pause > reviever_logs.txt &
nohup java ./Backends/StartBackend2 pause > heartbeat_sender.txt &
nohup java ./lb/LoadbalancerUpdated pause > monitor_logs.txt &