package Aufgabe2;

import java.util.concurrent.Semaphore;

public class VerwaltungLeserSchreiber {

    int currentWPosition = 0;
    int currentRPosition = 0
    int ctrWaitingWriters = 0;
    int ctrWriters = 0;
    int ctrWaitungReaders = 0;
    int ctrReader = 0;
    boolean[] waitingWriters = new boolean[5];
    boolean[] WaitingReaders= new boolean[5];
    Semaphore mutex = new Semaphore(1, true);
    Semaphore[] privSemL = new Semaphore[5];
    Semaphore[] privSemW = new Semaphore[5];

    //Methoden

    public void releaseWriter(){
        for(int i=currentWPosition;i<Wa.length;i++){
            currentWPosition = (currentWPosition+1)%waiting.lenght;
            if(waitingWriters[i]){
                privSemW[i].release();
                break;
            }
        }
    }


    public String read(int id) {

        try {
            mutex.acquire();

        if (ctrWriters < 1) {
            privSemL[id].release();
//command[id]
        }
        ctrWaitungReaders++;
        WaitingReaders[id] = true;
        mutex.release();


        privSemL[id].acquire();

            mutex.acquire();

        ctrReader++;
        ctrWaitungReaders--;
        WaitingReaders[id] = false;
        mutex.release();


//Sachen lesen Code


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


    public void Write(int ID) {
        mutex.acquire();
        if (anzSchreiber < 1 || ansLeser < 1) {
            privSemW[id].release()
        }
        ctrWaitingWriters++;
        waitingWriters[id] = true;
        mutex.realese();


        privSemW[id].acquire();
        mutex.acquire();
        ctrWriters++;
        ctrWaitungWriters--;
        waitingWriters[id] = false;
        mutex.release();

        ctrWaitingWriters--;
//Sachen schreiben...

        mutex.acquire();
        if (ctrWartendeLeser > 0) {
            releaseReader();
            ctrReader++;
        } else {
            releaseWriter();
        }




        //psvm und initialisieren der Semaphor arrays mit smeporen mit 0



    }
}
