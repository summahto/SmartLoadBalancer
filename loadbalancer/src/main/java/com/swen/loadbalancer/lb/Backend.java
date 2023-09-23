package com.swen.loadbalancer.lb;

public class Backend {
    // private URL url;

    private static final long MAX_WAIT_TIME_IN_MILLISECONDS = 30000;

    private String hostname;
    private String port;
    private long lastHeartBeatTimeUpdate;
    // private boolean alive;

    public Backend(String hostname, String port, long milliseconds) {
        // this.url = URL;
        this.hostname = hostname;
        this.port = port;
        this.lastHeartBeatTimeUpdate = milliseconds;
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

    public void setLastHeartBeatTimeUpdate(long lastHeartBeatTimeUpdate) {
        this.lastHeartBeatTimeUpdate = lastHeartBeatTimeUpdate;
    }

    public boolean isBackendAlive() {

        return System.currentTimeMillis() - lastHeartBeatTimeUpdate <= MAX_WAIT_TIME_IN_MILLISECONDS;
    }

    // public URL getUrl() {
    // return url;
    // }

}
