package severbenkh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverBENKH {

    private static ServerSocket serverSocket;
    private static final int PORT = 4321;

    public static void main(String[] args) throws IOException {

        String directoryName="D:\\ClientServer-Manage-Projects-master\\SeverBENKH\\src\\Users Information";
        File dir=new File(directoryName);
        if(!dir.exists()){
            dir.mkdir();
            
        }
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
