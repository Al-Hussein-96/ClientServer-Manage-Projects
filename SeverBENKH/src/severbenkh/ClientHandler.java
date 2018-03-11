package severbenkh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    private Socket client;
    private BufferedReader input;
    private PrintWriter output;

    ClientHandler(Socket client) {
        this.client = client;
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(),true);
        } catch (IOException ex) {
            System.out.println("Error in  Constucte");
        }
    }
    @Override
    public  void run(){
        String received = null;
        do{
            try {
                received = input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            output.println("ECHO: " + received);
            
        }while(!received.equals("Stop"));
        if(client != null)
        {
            try {
                System.out.println("Closing connection...");
                client.close();
            } catch (IOException ex) {
                System.out.println("Unable to disconnect");
            }
        }
        
        
    }

}












