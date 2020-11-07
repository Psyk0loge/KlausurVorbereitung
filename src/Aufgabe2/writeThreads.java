package Aufgabe2;

public class writeThreads extends Thread{
    private int id;
    public writeThreads(int id){
        this.id=id;
    }
    public void run(){

        while(true) {
            System.out.println("I bims Schreiber" + this.id + " und schlafe bis ich wieder schreibe");
            try {
                int sleepTime = (int) (Math.random()*10000);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VerwaltungLeserSchreiber.write(this.id);
        }




    }
}
