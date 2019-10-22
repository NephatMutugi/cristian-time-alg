package Neph;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientProcess {
    public static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
    public static long Timer;
    private static String serverName;
    private static int serverPort;
    private static long NewTime;

    public static class InternalInaccuteClock extends Thread{
        private long Drift;
        public InternalInaccuteClock(long how_inaccurate){
            Drift = how_inaccurate;
            Timer = System.currentTimeMillis();
        }
        public void run(){
            while (true){
                try {
                    Thread.sleep(1000+Drift);
                    Timer += 1000;
                    System.out.println(SDF.format(Timer));
                }
                catch (InterruptedException ex){
                    Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        }
    }

    public ClientProcess(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static class Update{
        public void run() throws IOException{
            Socket client;
            client = new Socket(serverName, serverPort);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            int i = 10;
            out.writeInt(10);
            for (int j = 0; j<i; j++){
                NewTime+=in.readLong();
            }
            NewTime/=i;

            Timer = NewTime;
            NewTime = 0;
            System.out.println("The new time is "+SDF.format(Timer));
            client.close();
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException{
        //TODO code application logic here
        Scanner input = new Scanner(System.in);
        long clockinaccuracy;
        System.out.println("How inaccurate is the clock?");
        clockinaccuracy = input.nextLong();
        String serverName = "localhost";
        int serverPort = 1333;
        ClientProcess client = new ClientProcess(serverName, serverPort);
        ClientProcess.InternalInaccuteClock C = new InternalInaccuteClock(clockinaccuracy);
        ClientProcess.Update update = new Update();

        System.out.println("How often check the clock");
        long checkClock = input.nextLong();
        while (true){
            Thread.sleep(checkClock);
            update.run();
        }
    }
}
