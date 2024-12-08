package com.example.travel_app_client;

import java.io.*;
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

            socket = new Socket(serverHost, serverPort);

            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to fetch offers");
            oos.writeObject("fetchOffers");

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
    public void bookOffer(int id) {
        try {
              // Ensure the socket is connected
            Socket socket = new Socket(serverHost, serverPort);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending integer to the server");

            oos.writeObject("bookOffer");
            oos.writeInt(id);

            String username = "tester";
            oos.writeUTF(username);
            oos.flush();
            // Optionally, read the response from the server
//            String response = (String) ois.readObject();  // Read response from the server
//            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
