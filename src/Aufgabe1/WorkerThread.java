package Aufgabe1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WorkerThread extends Thread{
    private DatagramPacket threadPackage;
    private DatagramSocket serverSocket;

    public WorkerThread(DatagramPacket threatPackage, DatagramSocket serverSocket){
        this.threadPackage =threatPackage;
        this.serverSocket=serverSocket;
    }

    public void run(){
        try {
        byte[] clientBuffer = new byte[65507];
        DatagramPacket clientPackage = new DatagramPacket(clientBuffer, clientBuffer.length);
        InetAddress clientAdress = clientPackage.getAddress();
        int clientPort = clientPackage.getPort();
        String msg = new String(clientBuffer,0, clientBuffer.length);
        //verarbeitung der Message
        // Nachricht Bsp: READ file1,2

        //Monitor
        String[] msgSplit = msg.split(" ",2);
        //1. READ/WRITE      2. file1,1
        if(msgSplit[0]=="READ"){
            String[] penis = msgSplit[1].split(",",2);
            String fileName = penis[0];
            String lineNo = penis[1];
            //Aufruf der readmethode des files mit den übergabeparamter lineNO und fileName


        }else if(msgSplit[0]=="WRITE"){
//            file1,1,Meine Note ist ne 1
            String[] penis = msgSplit[1].split(",",3);
            String fileName = penis[0];
            String lineNo = penis[1];
            String dataToWrite = penis[2];
            //Aufruf der Write-methode des files mit den Übergabeparamter lineNO und fileName und den Daten die in das Fiel geschreiben werden sollen


        }else{
            System.out.println("Befehl unbekannt");
        }



        //Zurücksenden
        String antwort = "";
        byte[] threadBuffer = new byte[65507];
        threadBuffer = antwort.getBytes();
        DatagramPacket threadPackage = new DatagramPacket(threadBuffer,threadBuffer.length, clientAdress,clientPort);
        serverSocket.send(threadPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
