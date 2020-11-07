package Aufgabe1;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.readAllLines;

public class MonitorKlasse {
    private int anzWartendeLeser =0;
    private int anzAktiverLeser =0;
    private int anzWartenderSchreiber =0;
    private int anzAktiverSchreiber =0;
    public static MonitorKlasse mKO = new MonitorKlasse();
    private static String[] fileName;
    private static File[] fileArray;
    public static String FILES_FOLDER_PATH = "C:\\Users\\Manolo Monetha.SOBIS-DE\\Google Drive\\Sync Folder\\DHBW Mannheim\\3.Semester\\Advanced IT\\Files";        //Pfad zu dem Ordner in dem die Dateien liegen...
    private String filePath;       //Pfad zu dem MyFile Objekt



    public synchronized void startRead(){
        //Schreiberpriorität
//        while(anzAktiverSchreiber >0||anzWartenderSchreiber>0){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //LeserPriorität nur warten wenn gerade jemand am schreiben ist weil wir sollen vor dem wartendenSchreiber drankommen
        while(anzAktiverSchreiber >0){
            try {
                anzWartendeLeser++;
                wait();
                anzWartendeLeser--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public synchronized void endRead(){
        anzAktiverLeser--;
        notifyAll();
    }
    public synchronized void startWrite(){
        //Schreiberpriorität
//        while(anzAktiverLeser>0||anzAktiverSchreiber>0){
//            try {
//                anzWartenderSchreiber++;
//                wait();
//                anzWartenderSchreiber--;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //Leserpriotität
        while(anzAktiverLeser>0||anzAktiverSchreiber>0||anzWartendeLeser>0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void endWrite(){
        anzAktiverSchreiber--;
        notifyAll();
    }


    public String read(int lineNo){
        startRead();
        Path filePathAsPath = Paths.get(filePath);//Speichern des Dateipfades als Pfad
        List<String> allLines;//zur Speicherung aller Dateizeilen
        String fileLine = null;//zur Speicherung der zu lesenden Zeile
        int index = lineNo-1;//zum Abruf der zu lesenenden Zeile aus allLines
        try{
            allLines = readAllLines(filePathAsPath);//einlesen und speichern aller Dateizeilen
            System.out.println("Lese die Zeilendaten in der Zeile \"" + lineNo + "\" !");
            fileLine = allLines.get(index);//speichern der zu lesenden Zeile
            String[] splitLine = fileLine.split(Integer.toString(lineNo),2);//teilen der gelesenen Zeile in Zeilennummer und Zeilentext
            fileLine = splitLine[1].trim();//speichern des reinen Zeilentextes ohne führende Leerzeichen
            System.out.println("Zeilendaten \"" + fileLine + " \"erfolgreich gelesen!");
        } catch (Exception e) {
            System.out.println("Gab ein Problem");
            return "Error";                                             //falls die Datei nicht existiert Rückgabe einer entsprechender Nachricht
        } finally {
            endRead();                                                 //Austrittsprotokoll - Lesen
        }
        return "";
    }

    public String write(int lineNo, String data){
        startWrite();
        Path filePathAsPath = Paths.get(filePath);//Speichern des Dateipfades als Pfad
        List<String> allLines;//zur Speicherung aller Dateizeilen
        int index = lineNo-1;//zum Abruf der zu lesenenden Zeile aus allLines
        try {
            allLines = readAllLines(filePathAsPath);//einlesen und speichern aller Dateizeilen
            String currentFileLine = allLines.get(index);//speichern der zu ändernden Zeile
            String[] splitFileNoAndLine = currentFileLine.split(Integer.toString(lineNo),2);//teilen der zu ändernden Zeile in Zeilennummer und Zeilentext
            currentFileLine = splitFileNoAndLine[1].trim();//speichern des reinen Zeilentextes ohne führende Leerzeichen
            System.out.println("Ersetze die Zeilendaten \"" + currentFileLine + "\" in der Zeile \"" + lineNo + "\" mit den neuen Zeilendaten \"" + data + "\" !");
            allLines.set(index,lineNo + " " + data);//überschreiben der Zeile in der Liste aller Zeilen
            Files.write(filePathAsPath,allLines);//überschreiben der Dateizeilen mithilfe der Liste aller Zeilen samt der geänderten Zeile
            System.out.println("Zeilendaten erfolgreich ersetzt!");
        } catch (Exception e) {
            System.out.println("Ne das gibt es nicht");
            return "Fehler";//falls die Datei nicht existiert Rückgabe einer entsprechender Nachricht
        } finally {
            endWrite();//Austrittsprotokoll - Schreiben
        }
        return "Wurde erfolgreich ersetzt";                                                //falls alles funktioniert hat Rückgabe der zu lesenden Zeile

    }



}
