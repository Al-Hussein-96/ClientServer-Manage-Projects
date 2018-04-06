package severbenkh;

import CommonClass.ViewfolderClass;
import CommonClass.CommonProject;
import CommonClass.NameAndDirectory;
import CommonCommand.*;
import CommonRespone.*;
import static CommonRespone.ResponeType.DONE;
import static CommonRespone.ResponeType.FALIURE;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.SeverBENKH.projectdirectoryName;

public class ClientHandler extends Thread {

    public static String MyUser;
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    ClientHandler(Socket client) {
        this.client = client;

        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));

        } catch (IOException ex) {
            System.out.println("Error in  Constucte");
        }

    }

    @Override
    public void run() {

        Command command = null;
        do {
            try {
                command = (Command) input.readObject();
            } catch (IOException ex) {
                System.out.println("Cann't Read Command");
                break;
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (command == null) {
                continue;
            }
            System.out.println("Command : " + command);

            switch (command.TypeCommand) {
                case SIGNUP:
                    SendToSignUp(command);
                    break;
                case LOGIN:
                    SendToLogin(command);
                    break;
                case STARTPROJECT:
                    SendToStartProject(command);
                    break;
                case MYPROJECT:
                    SendToMyProject(command);
                    break;
                case ALLPROJECT:
                    SendToAllProject(command);
                    break;
                case GETPROJECT:
                    GETPROJECT(command);
                    break;
                case GETFILE:
                    GETFILE(command);
                    break;
                case GETBRANCH:
                    GetBranch(command);
                    break;
                case GETCOMMITS:
                    GetCommits(command);
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

    private void GETFILE(Command command) {
        FileInputStream fis = null;
        try {
            byte[] DataFile = new byte[4096];
            File file = new File(((GetFile) command).getDirectoryFile());
            fis = new FileInputStream(file);
            long fileSize = file.length();

            int n;
            while (fileSize > 0 && (n = fis.read(DataFile, 0, (int) Math.min(4096, fileSize))) != -1) {
                fileSize -= n;
                Respone respone = new SendFile(DataFile,fileSize == 0);
                output.writeObject(respone);
                output.flush();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void GETPROJECT(Command command) {
        /// Get last Commit in master branch
        String NameProject = ((GetProject) command).NameProject;
        try {
            Project myprojecProject = get_projectClass(NameProject);
            for (branchClass br : myprojecProject.branchListClass) {
                if (br.branchName.equals("Master")) /// get Branch
                {
                    /// get Last Commit
                    int sz = br.way.size() - 1;
                    ViewfolderClass ob = ResourceManager.ViewProject(new File(br.way.get(sz).Directory));
                    SendProject Rc = new SendProject(ob);
                    output.writeObject(Rc);
                    output.flush();
                }
            }
        } catch (Exception ex) {
            Respone Rc = new SendStatus(ResponeType.FALIURE);
            try {
                output.writeObject(Rc);
                output.flush();
            } catch (IOException ex1) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
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

    private void SendToSignUp(Command command) {
        try {
            boolean ok = SignUpClass.SignUp(((SIGNUP) command).user);
            if (ok) {
                MyUser = ((SIGNUP) command).user.getName();
                System.out.println("Ok");

                output.writeObject(new SendStatus(DONE));
                output.flush();
            } else {
                output.writeObject(new SendStatus(FALIURE));
                output.flush();
            }
        } catch (IOException ex) {
            System.out.println("Error SIGNUP");
        }
    }

    private void SendToLogin(Command command) {
        System.out.println("SendToLogin");
        try {
            System.out.println("GOOO");
            boolean ok = LoginClass.Login(((LOGIN) command).user);
            if (ok) {
                MyUser = ((LOGIN) command).user.getName();
                output.writeObject(new SendStatus(DONE));
                output.flush();
            } else {
                output.writeObject(new SendStatus(FALIURE));
                output.flush();
            }
        } catch (IOException ex) {
            System.out.println("Error LOGIN");
        }
    }

    private void SendToStartProject(Command command) {
        try {
            boolean Access;
            String Author = MyUser;
            String ProjectDirectory = SeverBENKH.projectdirectoryName;
            String NameProject = ((StartProject) command).NameProject;
            String tempAccess = ((StartProject) command).Access;
            if ("true".equals(tempAccess)) {
                Access = true;
            } else {
                Access = false;
            }
            boolean ok = CanAddNewProjectToServer(NameProject);
            if (!ok) {
                output.writeObject(new SendStatus(FALIURE));
                output.flush();
                return;
            }
            output.writeObject(new SendStatus(DONE));
            output.flush();
            Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);
            SendCreateProject Rc = new SendCreateProject(NewProject.id, Author);

            output.writeObject(Rc);
            output.flush();
            System.out.println("SendTpStartProject");

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

    private void SendToMyProject(Command command) {
        /// for Send To Client
        try {
            List< CommonProject> MyProject = GetMyProject();

            SendMyProject Rc = new SendMyProject(MyProject);
            output.writeObject(Rc);
            output.flush();
            System.out.println("After");
        } catch (IOException ex) {
            try {
                output.writeObject(new SendStatus(FALIURE));
                output.flush();
            } catch (IOException ex1) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void SendToAllProject(Command command) {
        /// for Send To Client
        try {
            List< CommonProject> AllProject = GetAllProject();
            SendAllProject Rc = new SendAllProject(DONE, AllProject);
            output.writeObject(Rc);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void GetBranch(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void GetCommits(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
