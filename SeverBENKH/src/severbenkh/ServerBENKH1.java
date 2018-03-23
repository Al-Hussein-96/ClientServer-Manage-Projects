package severbenkh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBENKH1 {

    private static ServerSocket serverSocket1;
    private static final int PORT1 = 4322;
    
    public static void main(String[] args) throws IOException {
         try {
            serverSocket1 = new ServerSocket(PORT1);
        } catch (IOException ex) {
            System.out.println("Unable to Connect to Port");
            System.exit(1);
        }
        do {
            Socket client = serverSocket1.accept();
            System.out.println("New Client Accepted for File");

            ClientHandler1 handler = new ClientHandler1(client);
            handler.start();
        } while (true);
    }

}
