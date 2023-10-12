package com.swen.loadbalancer.Backends;

public class StartBackend2 {

    public static void main(String[] args) {
        HeartbeatSender heartbeatSender1 = new HeartbeatSender(6001, 9000);
        BackendRunnable backendRunnable1 = new BackendRunnable(9000, heartbeatSender1);

        // sending heartbeat to loadbalancer listening on port 6000
        backendRunnable1.getHeartbeatSender().start();

        // start backend which listens on port number 7000
        Thread backend1 = new Thread(backendRunnable1);
        backend1.start();

    }

}

// package com.swen.loadbalancer.Backends;

// import java.io.IOException;
// import java.net.ServerSocket;
// import java.util.concurrent.TimeUnit;

// public class StartBackend2 {

//     public static void main(String[] args) {
//         int heartbeatPort = 6001;
//         int backend1Port = 7001;
//         int backendPort = 9000;

//         while (isPortDown(backend1Port)) {
//             System.out.println("Heartbeat reciever is busy... Backend 2 is waiting to send heartbeats...");
//             try {
//                 TimeUnit.SECONDS.sleep(5); // Wait for 5 seconds before checking again
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }

//         // Port is available, start the heartbeat
//         HeartbeatSender heartbeatSender1 = new HeartbeatSender(heartbeatPort, backendPort);
//         BackendRunnable backendRunnable1 = new BackendRunnable(backendPort, heartbeatSender1);

//         // Sending heartbeat to the load balancer listening on port 6000
//         backendRunnable1.getHeartbeatSender().start();

//         // Start the backend which listens on port number 7000
//         Thread backend1 = new Thread(backendRunnable1);
//         backend1.start();
//     }

//     private static boolean isPortDown(int port) {
//         try (ServerSocket socket = new ServerSocket(port)) {
//             return false;
//         } catch (IOException e) {
//             return true;
//         }
//     }
// }
