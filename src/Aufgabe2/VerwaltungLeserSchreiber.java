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
        for(int i=0;i<5;i++){
            if(waitingWriters[currentWPosition]){
                ctrWriters++;
                ctrWaitingWriters--;
                waitingWriters[currentWPosition] = false;
                currentWPosition = (currentWPosition+1)%waitingWriters.length;
                privSemW[currentWPosition].release();
                break;
            }else{
                currentWPosition = (currentWPosition+1)%waitingWriters.length;
            }

        }
        System.out.println("Schreiber "+(currentWPosition)+" wurde aufgeweckt");
    }

    public static void releaseReader(){
        for(int i=0;i<5;i++){
            if(waitingReaders[currentRPosition]){
                ctrReader++;
                ctrWaitingReaders--;
                waitingReaders[currentRPosition] = false;
                currentRPosition = (currentRPosition+1)%waitingReaders.length;
                privSemL[currentRPosition].release();
                break;
            }else{
                currentRPosition = (currentRPosition+1)%waitingReaders.length;
            }
        }
        System.out.println("Leser "+(currentRPosition)+" wurde aufgeweckt");
    }


    public static void read(int id) {
        try {
        mutex.acquire();
            System.out.println("Leser "+id+" versucht zu lesen");
        if (ctrWriters < 1) {
        ctrReader++;
        privSemL[id].release();
        } else {
            ctrWaitingReaders++;
            waitingReaders[id] = true;
            System.out.println("Leser " + id+" muss warten");
        }
        mutex.release();

        privSemL[id].acquire();

        System.out.println("Leser " + id+" liest gerade");
        int sleepTime = (int) (Math.random()*1000);
        Thread.sleep(sleepTime);

        mutex.acquire();
        //nächsten Auswählen
        ctrReader--;
        System.out.println("Leser " + id+" hört auf zu lesen\n------------------------------------------------------");
        System.out.println("Anzahle leser: "+ctrReader+"\n Anzahle wartenderLeser "+ctrWaitingReaders+ "\n Anzahl SChreiber "+ctrWriters+"\n Anzahl wartender Schreiber " +ctrWaitingWriters );
        if(ctrWaitingReaders>0){
            releaseReader();
        } else if (ctrReader == 0 && ctrWaitingWriters > 0){
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
            mutex.acquire();
            System.out.println("Schreiber " + id+" versucht zu schreiben");
            if(ctrReader < 1 && ctrWriters < 1&&ctrWaitingReaders<1) {
                ctrWriters++;
                privSemW[id].release();
            }else {
                ctrWaitingWriters++;
                waitingWriters[id] = true;
                System.out.println("Schreiber " + id+" muss warten");
            }
            mutex.release();

            privSemW[id].acquire();

            System.out.println("Schreiber "+ id + " schreibt gerade");
            int sleepTime = (int) (Math.random()*1000);
            Thread.sleep(sleepTime);

            mutex.acquire();
            ctrWriters--;
            System.out.println("Schreiber " + id+" hört auf zu schreiben\n------------------------------------------------------");
            System.out.println("Anzahle leser: "+ctrReader+"\n Anzahle wartenderLeser "+ctrWaitingReaders+ "\n Anzahl SChreiber "+ctrWriters+"\n Anzahl wartender Schreiber " +ctrWaitingWriters );
            if (ctrWaitingReaders > 0) {
                releaseReader();
            } else {
                releaseWriter();
            }
            mutex.release();
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        System.out.println("Start");
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
