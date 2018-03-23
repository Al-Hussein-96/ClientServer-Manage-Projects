package severbenkh;

import CommonClass.CommitClass;
import CommonClass.CommonProject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.CommonServer.MyUser;

public class ClientHandler extends Thread {

    // public static String MyUser;
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
                case "STARTPROJECT":
                    SendToStartProject();
                    break;
                case "MYPROJECT":
                    SendToMyProject();
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
                CommonServer.MyUser = name;
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
                MyUser = name;
                output.println("Login Done Correct");
            } else {
                output.println("username or password is incorrect");
            }
        } catch (IOException ex) {
            System.out.println("Error LOGIN");
        }
    }

    private void SendToStartProject() {
        try {
            output.println("Done");
            boolean Access;
            String Author = MyUser;
            String NameProject = input.readLine();
            /// Should not send Directory
            /// String ProjectDirectory = input.readLine();
            String ProjectDirectory = SeverBENKH.projectdirectoryName;
            String tempAccess = input.readLine();
            if ("true".equals(tempAccess)) {
                Access = true;
            } else {
                Access = false;
            }
            System.out.println(Author + " : " + NameProject + " : " + ProjectDirectory);

            boolean ok = CanAddNewProjectToServer(NameProject);
            if (!ok) {
                System.out.println("Can't create project ");
                return;
            }
            Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// to see if this name is use before
    private boolean CanAddNewProjectToServer(String s) {
        String dir = SeverBENKH.projectdirectoryName;
        File projects = new File(dir);
        for (File t : projects.listFiles()) {
            if (t.isDirectory()) {
                if (t.getName().equals(s)) {
                    return false;
                }
            }
        }
        return true;

    }

    private void SendToMyProject() {
        System.out.println("Sent Done Only");
        output.println("Done");
    }
}
