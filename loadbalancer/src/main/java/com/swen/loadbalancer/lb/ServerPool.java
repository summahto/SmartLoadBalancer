package com.swen.loadbalancer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerPool {
    private List<Backend> backends;
    private AtomicInteger current;

    public ServerPool() {
        this.backends = new ArrayList<>();
        this.current = new AtomicInteger(0);
    }

    public void addBackend(URL backendURL) {
        Backend backend = new Backend(backendURL);
        backends.add(backend);
    }

    public Backend getNextPeer() {
        int next = current.incrementAndGet() % backends.size();
        int startIndex = next;
        do {
            Backend backend = backends.get(next);
            if (backend.isAlive()) {
                return backend;
            }
            next = (next + 1) % backends.size();
        } while (next != startIndex);

        return null;
    }

    public void markBackendStatus(URL backendURL, boolean alive) {
        for (Backend backend : backends) {
            if (backend.getUrl().equals(backendURL)) {
                backend.setAlive(alive);
                break;
            }
        }
    }

    public void healthCheck() {
        for (Backend backend : backends) {
            boolean alive = isBackendAlive(backend.getUrl());
            backend.setAlive(alive);
        }
    }

    private boolean isBackendAlive(URL url) {
        // Implement the logic to check if the backend is alive (e.g., TCP connection)
        // Return true if it's alive, false otherwise
        System.out.println("Checking whether the backend is alive or not : " + url.toString());
        return true;
    }
}
