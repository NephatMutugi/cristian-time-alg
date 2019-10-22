package Neph;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerProcess extends Thread {
    private final ServerSocket serverSocket;
    public ServerProcess(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }

    public void run(){
        while (true){
            try {
             String localHostName = java.net.InetAddress.getLocalHost().getHostName();
                Socket server = serverSocket.accept();
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                int i = in.readInt();
                for (int j = 0; j < i; j++){
                    Thread.sleep((long)(100+new Random().nextInt(51)));
                    out.writeLong(System.currentTimeMillis());
                }
                server.close();
            }
            catch (UnknownHostException ex){
                Logger.getLogger(ServerProcess.class.getName()).log(Level.SEVERE,null,ex);
            }
            catch (IOException | InterruptedException ex){
                Logger.getLogger(ServerProcess.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */

    public static void main(String [] args) throws IOException{
        int port = 1333;
        Thread thread = new ServerProcess(port);
        thread.start();
    }
}
