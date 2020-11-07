package Aufgabe1;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import static java.net.InetAddress.getLocalHost;

public class Client {

    public static DatagramSocket clientSocket = null;


    public static void main(String[] args) throws IOException {


        while (true) {
            try {
                clientSocket = new DatagramSocket();
                System.out.println("Client-Socket gestartet");
                System.out.println("Bitte geben Sie einen String ein: ");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String msg = bufferedReader.readLine();
                byte[] bytes = msg.getBytes();
                //byte[] bytes = new byte[65507];
                InetAddress inetAddress = clientSocket.getInetAddress();

                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,getLocalHost() , 5000);
                clientSocket.send(datagramPacket);
                byte[] serverBuffer = new byte[65507];//Puffer für die Nachricht des Servers
                DatagramPacket serverPacket = new DatagramPacket(serverBuffer, serverBuffer.length);//Vorbereitung zum Empfang der Servernachricht
                clientSocket.receive(serverPacket);//Empfangen der Servernachricht
                System.out.println("Serverantwort an Client: " + new String(serverPacket.getData(), 0, serverPacket.getLength()));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
