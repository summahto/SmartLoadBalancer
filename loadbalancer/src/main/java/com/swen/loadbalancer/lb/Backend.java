package com.swen.loadbalancer.lb;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class Backend {
    // private URL url;

    private static final long MAX_WAIT_TIME_IN_MILLISECONDS = 30000;

    private String hostname;
    private String port;
    private long lastHeartBeatReceivedTime;
    private BlockingQueue<String> messageQueue;
    // private boolean alive;

    public Backend(String hostname, String port, BlockingQueue<String> queue) {
        // this.url = URL;
        this.hostname = hostname;
        this.port = port;
        this.messageQueue = queue;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public long getLastHeartBeatReceivedTime() {
        return lastHeartBeatReceivedTime;
    }

    public BlockingQueue<String> getMessageQueue() {
        return messageQueue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Backend other = (Backend) obj;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (port == null) {
            if (other.port != null)
                return false;
        } else if (!port.equals(other.port))
            return false;
        return true;
    }

    public void setLastHeartBeatReceivedTime(long lastHeartBeatTimeUpdate) {
        this.lastHeartBeatReceivedTime = lastHeartBeatTimeUpdate;
    }

    public boolean isAlive() {

        return System.currentTimeMillis() - lastHeartBeatReceivedTime <= MAX_WAIT_TIME_IN_MILLISECONDS;
    }

    public void updateLastHeartbeatReceivedTime(long milliseconds) {

        System.out.println(
                "updating last Heartbeat received time for " + this.getPort() + " with " + milliseconds + " ms");
        this.lastHeartBeatReceivedTime = milliseconds;

    }

    // public URL getUrl() {
    // return url;
    // }

}
