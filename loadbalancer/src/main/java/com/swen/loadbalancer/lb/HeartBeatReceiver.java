package com.swen.loadbalancer.lb;

public abstract class HeartBeatReceiver {

    protected long checkingInterval;
    protected long checkingTime;
    protected long expireTime;
    protected long lastUpdatedTime;

    // update the lastUpdatedTime with the last received message
    public abstract void updateTime(long milliseconds);

    // current time - last updated time < max Waiting time (threshold) for aliveness
    // public abstract boolean checkAlive();

}
