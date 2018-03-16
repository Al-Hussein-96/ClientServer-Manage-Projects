package severbenkh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket client;
    private BufferedReader input;
    private PrintWriter output;

    ClientHandler(Socket client) {
        this.client = client;

        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error in  Constucte");
        }

    }

    @Override
    public void run() {
        String command = null;
        do {
            try {
                command = input.readLine();
            } catch (IOException ex) {
                System.out.println("Cann't Read Command");
                break;
            }

            if (command == null) {
                continue;
            }

            switch (command) {
                case "SIGNUP":
                    SendToSignUp();
                    break;
                case "LOGIN":
                    SendToLogin();
                    break;
                case "Send File":
                    break;

            }

        } while (!command.equals("Stop"));

        if (client != null) {
            try {
                System.out.println("Closing connection...");
                client.close();
            } catch (IOException ex) {
                System.out.println("Unable to disconnect");
            }
        }

    }

    private void SendToSignUp() {
        try {
            String name = input.readLine();
            String password = input.readLine();
            boolean ok = SignUpClass.SignUp(new User(name, password));
            
            
            
            if (ok) {
                output.println("Server Agree on username");
            } else {
                output.println("User Name is exist! please Change it");
            }
        } catch (IOException ex) {
            System.out.println("Error SIGNUP");
        }
    }

    private void SendToLogin() {
        try {
            String name = input.readLine();
            String password = input.readLine();
            boolean ok = LoginClass.Login(new User(name, password));
            if (ok) {
                output.println("Login Done Correct");
            } else {
                output.println("username or password is incorrect");
            }
        } catch (IOException ex) {
            System.out.println("Error LOGIN");
        }
    }

}
