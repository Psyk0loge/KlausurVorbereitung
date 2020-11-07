package Aufgabe1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Dispatcher {


    public static void main(String[] args) {
        try {
        final int DEFAULTPORT = 5000;
        DatagramSocket serverSocket = new DatagramSocket(DEFAULTPORT);

        while(true){
            byte[] byteBuffer = new byte[65507];
            DatagramPacket serverPacket = new DatagramPacket(byteBuffer, byteBuffer.length);
            serverSocket.receive(serverPacket);
            System.out.println("Packet recieved");
            WorkerThread test = new WorkerThread(serverPacket,serverSocket);
            test.start();
            System.out.println("Packet an Thread übergeben");

        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
