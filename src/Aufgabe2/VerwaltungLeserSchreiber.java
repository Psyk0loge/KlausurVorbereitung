package Aufgabe2;

import java.util.concurrent.Semaphore;

public class VerwaltungLeserSchreiber {

    private static int currentWPosition = 0;
    private static int currentRPosition = 0;
    private static int ctrWaitingWriters = 0;
    private static int ctrWriters = 0;
    private static int ctrWaitingReaders = 0;
    private static int ctrReader = 0;
    private static boolean[] waitingWriters = new boolean[5];
    private static boolean[] waitingReaders = new boolean[5];
    private static Semaphore mutex = new Semaphore(1, true);
    private static Semaphore[] privSemL = new Semaphore[5];
    private static Semaphore[] privSemW = new Semaphore[5];

    //Methoden

    public static void releaseWriter(){
        for(int i=currentWPosition;i<waitingWriters.length;i++){
            currentWPosition = (currentWPosition+1)%waitingWriters.length;
            if(waitingWriters[i]){
                privSemW[i].release();
                break;
            }
        }
    }

    public static void releaseReader(){
        for(int i=currentRPosition;i<waitingReaders.length;i++){
            currentRPosition = (currentRPosition+1)%waitingReaders.length;
            if(waitingReaders[i]){
                privSemL[i].release();
                break;
            }
        }
    }


    public static void read(int id) {
        try {

        System.out.println("Thread + " + id+" versucht zu lesen");
        mutex.acquire();
        if (ctrWriters < 1) {
        privSemL[id].release();
        }
        ctrWaitingReaders++;
        waitingReaders[id] = true;
        mutex.release();

        privSemL[id].acquire();
        mutex.acquire();
        ctrReader++;
        ctrWaitingReaders--;
        waitingReaders[id] = false;
        mutex.release();
        System.out.println("Thread + " + id+" liest gerade");

        mutex.acquire();
        //nächsten Auswählen
        ctrReader--;
        if (ctrReader == 0 && ctrWaitingWriters > 0) {
            releaseWriter();
        }
        mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void Write(int id) {
        try {
            mutex.acquire();
            if(ctrReader < 1 || ctrWriters < 1||ctrWaitingReaders<1) {
                privSemW[id].release();
            }
            ctrWaitingWriters++;
            waitingWriters[id] = true;
            mutex.release();


            privSemW[id].acquire();
            mutex.acquire();
            ctrWriters++;
            ctrWaitingWriters--;
            waitingWriters[id] = false;
            mutex.release();

            ctrWaitingWriters--;
//Sachen schreiben...

            mutex.acquire();
            if (ctrWaitingReaders > 0) {
                releaseReader();
                ctrReader++;
            } else {
                releaseWriter();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

    }
}
