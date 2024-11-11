package com.example.travel_app_client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class SocketClient {

    private String serverHost;
    private int serverPort;

    public SocketClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public List<Offer> fetchOffers() {
        List<Offer> offers = null;
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            // Establish socket connection to the server
            InetAddress host = InetAddress.getLocalHost();
            socket = new Socket(serverHost, serverPort);

            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to fetch offers");
            oos.writeObject("fetchOffers");

            // Read the server response
            ois = new ObjectInputStream(socket.getInputStream());

            try {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    offers = (List<Offer>) obj;
                    System.out.println("Received " + offers.size() + " offers.");
                    for (Offer offer : offers) {
                        System.out.println(offer);
                    }
                } else {
                    System.out.println("Received unexpected object type: " + obj.getClass());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return offers;
    }
}
