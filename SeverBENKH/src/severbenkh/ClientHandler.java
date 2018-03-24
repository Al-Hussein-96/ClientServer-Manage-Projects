package severbenkh;

import CommonClass.CommonProject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    public static String MyUser;
    private Socket client;
    private DataInputStream input;
    private DataOutputStream output;

    ClientHandler(Socket client) {
        this.client = client;

        try {
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error in  Constucte");
        }

    }

    @Override
    public void run() {

        String command = null;
        do {
            try {
                command = input.readUTF();
                System.out.println("Command: " + command);
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
            String name = input.readUTF();
            String password = input.readUTF();
            boolean ok = SignUpClass.SignUp(new User(name, password));

            if (ok) {
                MyUser = name;
                output.writeUTF("Server Agree on username");
            } else {
                output.writeUTF("User Name is exist! please Change it");
            }
        } catch (IOException ex) {
            System.out.println("Error SIGNUP");
        }
    }

    private void SendToLogin() {
        try {
            String name = input.readUTF();
            String password = input.readUTF();

            boolean ok = LoginClass.Login(new User(name, password));
            if (ok) {
                MyUser = name;
                output.writeUTF("Login Done Correct");

            } else {
                output.writeUTF("username or password is incorrect");

            }
        } catch (IOException ex) {
            System.out.println("Error LOGIN");
        }
    }

    private void SendToStartProject() {
        try {
            output.writeUTF("Done");
            //   output.println("Done");
            boolean Access;
            String Author = MyUser;
            String NameProject = input.readUTF();
            /// Should not send Directory
            /// String ProjectDirectory = input.readLine();
            String ProjectDirectory = SeverBENKH.projectdirectoryName;
            String tempAccess = input.readUTF();
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
        try {
            output.writeUTF("Done");
//        output.println("Done");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Recive Request from Client To Create list of MyProject");
        System.out.println("User: " + MyUser);
        List< CommonProject> MyProject = GetMyProject();

        System.out.println("List: " + MyProject.size());

        /// for Send To Client
        try {

            FileOutputStream fos = new FileOutputStream("temp.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(MyProject);
            oos.close();

            Path path = Paths.get("temp.data");

            byte[] buffer = Files.readAllBytes(path);
            output.write(buffer);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DONE SENT FILE FROM SERVER");

    }

    private List< Project> getAllProjectInServer() {
        List< Project> TempList = new ArrayList<>();
        String projectdirectoryName = "src\\Projects Information";
        File Allproject = new File(projectdirectoryName);
        ViewfolderClass Viewfolder = ResourceManager.ViewFolder(Allproject);
        for (String s : Viewfolder.MyFolder) {

            String sdirectoryName = s + "\\" + "info";
            try {
                FileInputStream fileIn = new FileInputStream(sdirectoryName);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Project obj = (Project) objectIn.readObject();
                TempList.add(obj);
                objectIn.close();
            } catch (IOException | ClassNotFoundException ex) {

            }
        }
        return TempList;
    }

    private List< CommonProject> GetMyProject() {
        List< CommonProject> MyProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (String t : s.Contributors) {
                if (t.equals("m")) {
                    ok = true;
                }
            }
            if (ok) {
                MyProject.add(Project_to_CommonProject(s));
            }
        }
        return MyProject;
    }

    private List< CommonProject> GetPublicProject() {
        List< CommonProject> PublicProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            if (s.Access == true) {
                PublicProject.add(Project_to_CommonProject(s));
            }
        }
        return PublicProject;
    }

    private CommonProject Project_to_CommonProject(Project MyProject) {
        CommonProject temp = new CommonProject();
        temp.Access = MyProject.Access;
        temp.Author = MyProject.Author;
        temp.Contributors = MyProject.Contributors;
        temp.NameProject = MyProject.NameProject;
        temp.id = MyProject.id;
        temp.numberOFBranshes = MyProject.numberOFBranshes;
        temp.DateCreate = MyProject.DateCreate;
        List<String> BranchNames = new ArrayList<>();
        for (branchClass s : MyProject.branchListClass) {
            BranchNames.add(s.branchName);
            System.out.println(temp.way.toString());
            for (CommonClass.CommitClass t : s.way) {
                temp.way.add(t);
            }
        }
        return temp;
    }

}
