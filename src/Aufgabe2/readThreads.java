package Aufgabe2;

import java.util.concurrent.Semaphore;

public class readThreads extends Thread{
    //Attribute
    private int id;

    public readThreads(int id){
        this.id=id;
    }

    public void run(){

        VerwaltungLeserSchreiber.read(this.id);
        System.out.println("I bims Readerthread" + this.id + "und schlafe bis ich wieder Lese");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
