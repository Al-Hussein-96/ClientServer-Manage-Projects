package severbenkh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class tempClient {

    private static InetAddress host;
    private static final int PORT = 4321;

    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("\nHost ID not foun!");
            System.exit(1);
        }
        sendMessage();
    }

    private static void sendMessage() {
        Socket socket = null;
        
        try {
            socket = new Socket(host,PORT);
            BufferedReader networkInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            PrintWriter networkOutput = new PrintWriter(socket.getOutputStream(),true);
            
            Scanner userEnrty = new Scanner(System.in);
            
            String message,response;
            
            do{
                System.out.println("Enter Message ('QUIT') to exit: ");
                message = userEnrty.nextLine();
                networkOutput.println(message);
                response = networkInput.readLine();
                
                System.out.println("\nServer : " + response);
                
            }while(!message.equals("QUIT"));
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                System.out.println("\nClosing connection...");
                socket.close();
            } catch (IOException ex) {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }

}
