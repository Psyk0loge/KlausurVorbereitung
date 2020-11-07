package Aufgabe2;

public class writeThreads extends Thread{
    private int id;
    public writeThreads(int id){
        this.id=id;
    }
    public void run(){

        VerwaltungLeserSchreiber.write(this.id);
        System.out.println("I bims Writerthread" + this.id + "und schlafe");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }
}
