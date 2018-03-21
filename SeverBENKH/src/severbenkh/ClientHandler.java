package severbenkh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataOutputStream;
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

    private void SendToMyProject() {
        List< CommonProject> MyProject = GetMyProject();

        try {

            output.println("Done");

            FileOutputStream fos = new FileOutputStream("temp.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(MyProject);
            oos.close();

            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            FileInputStream fis = new FileInputStream("temp.data");
            byte[] buffer = new byte[4096];

            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }

            fis.close();
            dos.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
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

    private List< CommonProject> GetMyProject() {
        List< CommonProject> MyProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (String t : s.Contributors) {
                if (t.equals(MyUser)) {
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
            for (CommitClass t : s.way) {
                temp.way.add(t);
            }
        }
        return temp;
    }

}
