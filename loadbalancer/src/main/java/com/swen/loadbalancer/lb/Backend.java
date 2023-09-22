package com.swen.loadbalancer.lb;

import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class Backend {
    private URL url;
    private boolean alive;
    private ReadWriteLock lock;
    private CloseableHttpClient reverseProxy;

    public Backend(URL URL) {
        this.url = URL;
        this.alive = false;
        this.lock = new ReentrantReadWriteLock();
        this.reverseProxy = HttpClients.createDefault();
    }

    // Getter and setter methods for URL, Alive, mux, and ReverseProxy

    public boolean isAlive() {
        lock.readLock().lock();
        try {
            return alive;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setAlive(boolean alive) {
        lock.writeLock().lock();
        try {
            this.alive = alive;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public URL getUrl() {
        return url;
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public CloseableHttpClient getReverseProxy() {
        return reverseProxy;
    }
}
