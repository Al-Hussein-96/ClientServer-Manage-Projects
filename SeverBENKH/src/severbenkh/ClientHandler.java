package severbenkh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.SeverBENKH.projectdirectoryName;

public class ClientHandler extends Thread {

    private String MyUser;
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
                MyUser = name;
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
            String ProjectDirectory = input.readLine();
            String tempAccess = input.readLine();
            /// 
            if ("true".equals(tempAccess)) {
                Access = true;
            } else {
                Access = false;
            }
            System.out.println(Author + " : " + NameProject + " : " + ProjectDirectory);

            Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);

            AddNewProjectToServer(NewProject);

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    *  This function to add the new project to all projects in the server in track (src\\Projects Information)
    *  
    *   when Client Create Project in GUI this function will call to add NewProject to Old Project information[
     */
    private void AddNewProjectToServer(Project NewProject) {

        try {
            ResourceManager.save((Serializable) NewProject, projectdirectoryName + "\\" + NewProject.NameProject);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private List< Project> getAllProjectInServer() {
        List< Project> TempList = new ArrayList<>();
        String projectdirectoryName = "src\\Projects Information";
        File Allproject = new File(projectdirectoryName);
        ViewfolderClass Viewfolder = ResourceManager.ViewFolder(Allproject);
        for (String s : Viewfolder.MyFolder) {
            String sdirectoryName = projectdirectoryName + "\\" + s + "info";
            try {
                FileInputStream fileIn = new FileInputStream(sdirectoryName);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Project obj = (Project) objectIn.readObject();
                TempList.add(obj);
                objectIn.close();
            } catch (Exception ex) {

            }
        }
        return TempList;
    }

    private List< Project> GetMyProject() {
        List< Project> MyProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (String t : s.Contributors) {
                if (t.equals(MyUser)) {
                    ok = true;
                }
            }
            if (ok) {
                MyProject.add(s);
            }
        }
        return MyProject;
    }

    private List< Project> GetPublicProject() {
        List< Project> PublicProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            if (s.Access == true) {
                PublicProject.add(s);
            }
        }
        return PublicProject;
    }

}
