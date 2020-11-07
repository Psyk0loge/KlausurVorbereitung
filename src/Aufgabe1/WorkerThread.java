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
        String antwort="";
        int clientPort = this.threadPackage.getPort();
        InetAddress clientAdress = this.threadPackage.getAddress();
        byte[] clientMsg = threadPackage.getData();
        String msg = new String(clientMsg,0, clientMsg.length);
            //verarbeitung der Message
            // Nachricht Bsp: READ file1,2

            //Monitor
        String[] msgSplit = msg.split(" ",2);
            System.out.println("1."+ msgSplit[0]+" 2."+msgSplit[1]);
        //1. READ/WRITE      2. file1,1
        if(msgSplit[0].equals("READ")){
            System.out.println("Thread liest");
            String[] penis = msgSplit[1].split(",",2);
            String fileName = penis[0];
            String lineNoS = penis[1].trim();
            int lineNo = Integer.parseInt(lineNoS);
            //Aufruf der readmethode des files mit den übergabeparamter lineNO und fileName
            MonitorKlasse.mKO.read(lineNo);


        }else if(msgSplit[0].equals("WRITE")){
            System.out.println("Thread schreibt");
//            file1,1,Meine Note ist ne 1
            String[] penis = msgSplit[1].split(",",3);
            String fileName = penis[0];
            int lineNo = Integer.parseInt(penis[1]);
            String dataToWrite = penis[2];
            //Aufruf der Write-methode des files mit den Übergabeparamter lineNO und fileName und den Daten die in das Fiel geschreiben werden sollen
            MonitorKlasse.mKO.write(lineNo,dataToWrite);


        }else{
            System.out.println("Befehl unbekannt");
        }



        //Zurücksenden

        byte[] threadBuffer;
        threadBuffer = antwort.getBytes();
        DatagramPacket threadPackage = new DatagramPacket(threadBuffer,threadBuffer.length, clientAdress,clientPort);
        serverSocket.send(threadPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
