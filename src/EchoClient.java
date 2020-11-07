import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

public class EchoClient {
    public static final int serverPort =8080;
    public static InetAddress penis;

    public static void main(String[] args) {
        //declare variables first
        String hostname ="localhost";
        PrintWriter networkOut = null;
        BufferedReader networkIn = null;

        Socket s = null;
        try{
            s = new Socket("localhost", serverPort);
            System.out.println("Connected to echo Server" );
            networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            networkOut = new PrintWriter(s.getOutputStream());

            while (true){
                String theLine = userIn.readLine();
                if(theLine.equals(".")){break;}
                networkOut.println(theLine);
                networkOut.flush();
                System.out.println(networkIn.readLine());
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(s !=null){
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
