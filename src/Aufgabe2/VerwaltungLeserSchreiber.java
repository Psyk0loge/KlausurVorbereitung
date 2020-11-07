package Aufgabe2;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

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
        System.out.println("Leser " + id+" versucht zu lesen");
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
        System.out.println("Leser " + id+" liest gerade");
        int sleepTime = (int) (Math.random()*1000);
        Thread.sleep(sleepTime);
        mutex.acquire();
        //nächsten Auswählen
        ctrReader--;
        System.out.println("Leser " + id+" hört auf zu lesen\n------------------------------------------------------");
        if (ctrReader == 0 && ctrWaitingWriters > 0) {
            releaseWriter();
        }
        mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void write(int id) {
        try {
            //Eintrittsprotokoll
            System.out.println("Schreiber " + id+" versucht zu schreiben");
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
            System.out.println("Schreiber "+ id + " schreibt gerade");  //Sachen schreiben...
            int sleepTime = (int) (Math.random()*1000);
            Thread.sleep(sleepTime);

            mutex.acquire();
            ctrWriters--;
            System.out.println("Schreiber " + id+" hört auf zu schreiben\n------------------------------------------------------");
            if (ctrWaitingReaders > 0) {
                releaseReader();
            } else {
                releaseWriter();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        //Aus den beiden Klassen die Threads erstellen
        readThreads[] readThreads = new readThreads[5];
        writeThreads[] writeThreads = new  writeThreads[5];
        for(int i=0;i<waitingReaders.length;i++){
            waitingReaders[i]=false;
        }
        for(int i=0;i<waitingWriters.length;i++){
            waitingWriters[i]=false;
        }
        for(int i=0;i<privSemL.length;i++){
            privSemL[i]=new Semaphore(0,true);
        }
        for(int i=0;i<privSemW.length;i++){
            privSemW[i]=new Semaphore(0,true);
        }

        for(int i=0;i<5;i++){
            readThreads[i] = new readThreads(i);
            readThreads[i].start();
        }

        for (int i=0;i<5;i++){
            writeThreads[i] = new writeThreads(i);
            writeThreads[i].start();
        }


    }
}
