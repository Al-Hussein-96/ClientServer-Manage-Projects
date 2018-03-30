package severbenkh;

import CommonClass.ViewfolderClass;
import CommonClass.ResourceManager;
import CommonClass.CommonProject;
import CommonClass.NameAndDirectory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import static severbenkh.SeverBENKH.projectdirectoryName;

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
                case "ALLPROJECT":
                    SendToAllProject();
                    break;
                case "GETPROJECT":
                    GETPROJECT();
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

    private void GETPROJECT() {

        try {
            output.writeUTF("Done");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //// get all branch  " GetAllBranch" + "Name"
        /// get name project "SendNameProject"
        while (true) {
            try {
                String temp = input.readUTF();
                switch (temp) {
                    case "SendNameProject":
                        SendNameProject();
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Project get_projectClass(String temp) {
        try {
            return (Project) ResourceManager.load(projectdirectoryName + "\\" + temp + "\\" + "info");
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void SendNameProject() {
        try {
            String temp = input.readUTF();
            try {
                Project myprojecProject = get_projectClass(temp);
                for (branchClass br : myprojecProject.branchListClass) {
                    if (br.branchName.equals("Master")) /// get Branch
                    {
                        /// get Last Commit
                        int sz = br.way.size();
                        sz--;

                         //// Done
                        System.out.println("Function SendNameProject in ClientHandler before Error");
                        System.out.println(br.projectdirector);
                        System.out.println((br.way.get(sz)).Directory);
                         ViewfolderClass ob = ResourceManager.ViewProject(new File(br.way.get(sz).Directory));
                         
                         System.out.println("Function SendNameProject in ClientHandler after Error");
                        //// Done
                        ResourceManager.ShowViewfolder(ob);
                       ///   SentObjectUseFile(ob);  /// not now, first must ob not null
                    }
                }
            } catch (Exception ex) {
            //    System.out.println("Error: " + ex.getMessage());

            }

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
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

            boolean Access;
            String Author = MyUser;
            String NameProject = input.readUTF();

            /// Should not send Directory
            /// String ProjectDirectory = input.readLine();
            /// this is Directory in Server
            String ProjectDirectory = SeverBENKH.projectdirectoryName;
            String tempAccess = input.readUTF();
            if ("true".equals(tempAccess)) {
                Access = true;
            } else {
                Access = false;
            }

            boolean ok = CanAddNewProjectToServer(NameProject);
            if (!ok) {
                output.writeUTF("Can't create project ");
                return;
            }
            Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);
            output.writeUTF("Done");
            output.writeInt(NewProject.id);
            output.writeUTF(Author);

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

    private void SentObjectUseFile(Object ob) {
        /// for Send To Client
        try {

            FileOutputStream fos = new FileOutputStream("temp.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ob);
            oos.close();

            Path path = Paths.get("temp.data");

            byte[] buffer = Files.readAllBytes(path);
            int Size = buffer.length;
            output.writeInt(Size);
            System.out.println("Size = " + Size);
            output.write(buffer);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DONE SENT FILE FROM SERVER");
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
            int Size = buffer.length;
            output.writeInt(Size);
            System.out.println("Size = " + Size);
            output.write(buffer);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DONE SENT FILE FROM SERVER");

    }

    private void SendToAllProject() {
        System.out.println("Sent Done Only");
        try {
            output.writeUTF("Done");
//        output.println("Done");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Recive Request from Client To Create list of AllProject");
        System.out.println("User: " + MyUser);
        List< CommonProject> AllProject = GetAllProject();

        System.out.println("List: " + AllProject.size());

        /// for Send To Client
        try {

            FileOutputStream fos = new FileOutputStream("temp.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AllProject);
            oos.close();

            Path path = Paths.get("temp.data");

            byte[] buffer = Files.readAllBytes(path);
            int Size = buffer.length;
            output.writeInt(Size);
            System.out.println("Size = " + Size);
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
        String projectdirectoryName = SeverBENKH.projectdirectoryName;
        File Allproject = new File(projectdirectoryName);
        ViewfolderClass Viewfolder = ResourceManager.ViewFolder(Allproject);
        for (NameAndDirectory s : Viewfolder.MyFolder) {
            String sdirectoryName = s.Directory + "\\" + "info";
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

    private List< CommonProject> GetAllProject() {
        List< CommonProject> AllProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (String t : s.Contributors) {
                if (t.equals(MyUser)) {
                    ok = true;
                }
            }
            if (ok || s.Access) {
                AllProject.add(Project_to_CommonProject(s));
            }
        }
        return AllProject;
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
