package severbenkh;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeverBENKH {

    int Login_Port = 1;
    int SiginUp_Port = 2;

    private static ServerSocket serverSocket;
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            System.out.println("Unable to Connect to Port");
            System.exit(1);
        }
        
        do{
            try {
                Socket client = serverSocket.accept();
                System.out.println("New Client Accepted");
                
                ClientHandler handler = new ClientHandler(client);
                handler.run();
            } catch (IOException ex) {
                System.out.println("Can't connect Server");
            }
            
        }while(true);
        
        
        

    }

    public void LoginPort() throws IOException {
        ServerSocket LoginPort = new ServerSocket(Login_Port);

        Socket ss = LoginPort.accept();
        Scanner sc = new Scanner(ss.getInputStream());

        User NewUser = new User();
        NewUser.Name = sc.nextLine();
        NewUser.Password = sc.nextLine();

//        String temp;
//        Scanner F = new Scanner(System.in);
//        temp = F.nextLine();
//        PrintStream p = new PrintStream(ss.getOutputStream());
//        p.println(temp);
    }

    public void SiginUpPort() throws IOException {
        ServerSocket SiginUpPort = new ServerSocket(SiginUp_Port);

        Socket ss = SiginUpPort.accept();
        Scanner sc = new Scanner(ss.getInputStream());

        User NewUser = new User();
        NewUser.Name = sc.nextLine();
        NewUser.Password = sc.nextLine();
//        
//        String temp;
//        Scanner F = new Scanner(System.in);
//        temp = F.nextLine();
//        PrintStream p = new PrintStream(ss.getOutputStream());
//        p.println(temp);

    }
}
