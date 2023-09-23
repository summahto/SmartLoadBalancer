package com.swen.loadbalancer.lb;

import java.util.HashMap;
import java.util.Map;

public class ServerPool {

    private static ServerPool SERVERPOOL_SINGLE_INSTANCE = null;

    private Map<String, Backend> backends;

    // Allowing only 1 serverpool instance to be created
    public static ServerPool getInstance() {
        if (SERVERPOOL_SINGLE_INSTANCE == null) {
            synchronized (ServerPool.class) {
                if (SERVERPOOL_SINGLE_INSTANCE == null) {
                    SERVERPOOL_SINGLE_INSTANCE = new ServerPool();
                }
            }
        }
        return SERVERPOOL_SINGLE_INSTANCE;
    }

    private ServerPool() {
        this.backends = new HashMap<>();
    }

    public void addBackend(String hostname, String port, long milliseconds) {

        System.out.println("updating last Heartbeat received time for " + port + " with " + milliseconds + " ms");
        if (backends.containsKey(port)) {
            backends.get(port).setLastHeartBeatTimeUpdate(milliseconds);
        } else {
            Backend backend = new Backend(hostname, port, milliseconds);
            backends.put(port, backend);

        }
    }

    /*
     * public Backend getNextPeer() {
     * int next = current.incrementAndGet() % backends.size();
     * int startIndex = next;
     * do {
     * Backend backend = backends.get(next);
     * if (backend.isAlive()) {
     * return backend;
     * }
     * next = (next + 1) % backends.size();
     * } while (next != startIndex);
     * 
     * return null;
     * }
     */

    /*
     * Not required as of now.
     * 
     * public void markBackendStatus(URL backendURL, boolean alive) {
     * for (Backend backend : backends) {
     * if (backend.getUrl().equals(backendURL)) {
     * backend.setAlive(alive);
     * break;
     * }
     * }
     * }
     */

}
