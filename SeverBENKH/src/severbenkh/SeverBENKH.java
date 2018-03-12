package severbenkh;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeverBENKH {

    private static ServerSocket serverSocket;
    private static final int PORT = 4321;

    public static void main(String[] args) throws IOException {
        
        /***************** temp *****************/
        int s = 1;
        try {
            ResourceManager.save(s, "Moh");
        } catch (Exception ex) {
            Logger.getLogger(SeverBENKH.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*****************************************/
        
        
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            System.out.println("Unable to Connect to Port");
            System.exit(1);
        }
        do {
            Socket client = serverSocket.accept();
            System.out.println("New Client Accepted");

            ClientHandler handler = new ClientHandler(client);
            handler.start();
        } while (true);

    }

  
}
