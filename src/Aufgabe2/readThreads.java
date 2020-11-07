package Aufgabe2;

import java.util.concurrent.Semaphore;

public class readThreads extends Thread{
    //Attribute
    private int id;

    public readThreads(int id){
        this.id=id;
    }

    public void run(){
        while(true) {
            System.out.println("I bims Leser" + this.id + " und schlafe bis ich wieder Lese");
            try {
                int sleepTime = (int) (Math.random()*10000);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VerwaltungLeserSchreiber.read(this.id);
        }

    }


}
