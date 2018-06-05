package severbenkh;

import CommonClass.ResourceManager;
import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import CommonClass.ViewfolderClass;
import CommonClass.CommonProject;
import CommonClass.Contributor;
import CommonClass.NameAndDirectory;
import CommonClass.Profile;
import CommonClass.ProjectToUpload;
import CommonClass.User;
import CommonCommand.*;
import CommonRespone.*;
import static CommonRespone.ResponeType.DONE;
import EventClass.Event_AddBranch;
import EventClass.Event_AddCommit;
import EventClass.Event_AddContributor;
import EventClass.Event_Class;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.SeverBENKH.list_user_in_server;
import static severbenkh.SeverBENKH.projectdirectoryName;
import static severbenkh.SeverBENKH.serverSocket;

public class ClientHandler extends Thread {

    public String MyUser;
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket client) {
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
            if (serverSocket.isClosed()) {
                break;
            }

            try {
                command = (Command) input.readObject();
                System.out.println("command: " + command.toString());
            } catch (IOException ex) {
                System.out.println("Cann't Read Command");
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (command == null) {
                continue;
            }
            /// select the command from client
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
                case GETPULL:
                    SendToGetPull(command);
                    break;
                case LISTBRANCH:
                    SendToListBranch(command);
                    break;
                case LISTCOMMITS:
                    SendToListCommits(command);
                    break;
                case ListCONTRIBUTORS:
                    SendToListContributors(command);
                    break;
                case GETPUSH:
                    SendToGetPush(command);
                    break;
                case ADDBRANCH:
                    SendToAddBranch(command);
                    break;
                case ADDCONTRIBUTOR:
                    SendToAddContributor(command);
                    break;

                case GETLISTUSER:
                    SendToListUsers(command);
                    break;
                case GETPROFILE:
                    SendToProfile(command);
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

    private void SendToListUsers(Command command) {
        try {
            List< User> temp = (List< User>) ResourceManager.load(list_user_in_server);
            SendListUser Rc = new SendListUser(temp);
            Send_Respone(Rc);
            /// here must Create Respone of (SendListUser) and SendIt
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SendToProfile(Command command) {
        String user = ((GetProfile) command).getUserName();
        Profile temp = get_profile(user);
        Respone respone = new SendProfile(temp);
        Send_Respone(respone);
    }

    private User get_information(String user) {
        User temp = null;
        String directoryname = SeverBENKH.usersdirectoryName + "\\" + user;
        String userFileName = directoryname + "\\" + user + "information file.data";
        try {
            temp = (User) ResourceManager.load(userFileName);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    private Profile get_profile(String user) {
        Profile temp = null;
        User NewUser = get_information(user);
        List< CommonProject> AllContributorProject = GetUserProject(user);
        List< CommonProject> ContributorProject = new ArrayList<>();
        List< CommonProject> OwnProject = new ArrayList<>();
        for (CommonProject t : AllContributorProject) {
            boolean MyUserInContributor = false;
            for (Contributor C : t.Contributors) {
                if (C.Name.equals(MyUser)) {
                    MyUserInContributor = true;
                    break;
                }
            }
            if (t.Access || MyUserInContributor) {
                ContributorProject.add(t);
            }
            if (t.Author.equals(user) && (t.Access || MyUserInContributor)) {
                OwnProject.add(t);
            }
        }
        temp = new Profile(NewUser, OwnProject, ContributorProject);
        return temp;
    }

    private void SendToAddContributor(Command command) {
        String NameProject = ((GetAddContributor) command).NameProject;
        Project Myproject = get_projectClass(NameProject);
        String UserName = ((GetAddContributor) command).getUserName();
        List< User> AllUsers = null;
        boolean UserNameExists = false;
        boolean MyUserInContributors = false;
        for (Contributor C : Myproject.Contributors) {
            if (C.Name.equals(MyUser)) {
                MyUserInContributors = true;
            }
        }
        if (!MyUserInContributors) {
            Send_FALIURE();
            return;
        }
        // check if the username exists
        try {
            AllUsers = (List< User>) ResourceManager.load(list_user_in_server);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (AllUsers != null) {
            for (User u : AllUsers) {
                if (u.getName().equals(UserName)) {
                    UserNameExists = true;
                    break;
                }
            }
            if (UserNameExists) {
                Myproject.add_Contributor(UserName);
                Myproject.Save();
                Event_Class Ev = new Event_AddContributor(MyUser, NameProject, new Date(), UserName);
                addNewEvent(Ev);
                Send_Done();
                return;
            }
        }
        Send_FALIURE();
    }

    private void SendToAddBranch(Command command) {

        ///  GetAddBranch
        String NameProject = ((GetAddBranch) command).NameProject;
        String BranchName = ((GetAddBranch) command).BranchName;
        String BranchFather = ((GetAddBranch) command).BranchFather;
        int idCommit = ((GetAddBranch) command).idCommit;
        Project Myproject = get_projectClass(NameProject);
        branchClass BranchFather_class = null;
        boolean MyUserInContributors = false;
        for (Contributor C : Myproject.Contributors) {
            if (C.Name.equals(MyUser)) {
                MyUserInContributors = true;
            }
        }
        if (!MyUserInContributors) {
            Send_FALIURE();
            return;
        }
        for (branchClass s : Myproject.branchListClass) {
            if (s.branchName.equals(BranchName)) {
                /// there is branch same name 
                Send_FALIURE();
                return;
            }
            if (s.branchName.equals(BranchFather)) {
                BranchFather_class = s;
            }
        }

        branchClass New = new branchClass(Myproject, BranchName, BranchFather_class, idCommit, MyUser);
        Myproject.branchListClass.add(New);
        Myproject.Save();

        Event_Class Ev = new Event_AddBranch(MyUser, NameProject, new Date(), BranchName);
        addNewEvent(Ev);
        Send_Done();
    }

    /// need code
    private void addNewEvent(Event_Class Ev) {

    }

    /// get file BENKH form server for this project and this branch 
    private ProjectToUpload get_ProjectToUpload(String ProjectName, String branchName) {
        ProjectToUpload temp = null;
        /// get project class for this project form .info 
        Project serverproject = get_projectClass(ProjectName);
        branchClass serverbranch = null;
        /// get select branch 
        for (branchClass s : serverproject.branchListClass) {
            if (s.branchName.equals(branchName)) {
                /// get last commit 
                int IdlastCommite = s.way.size() - 1;
                temp = s.BENKH_File.get(IdlastCommite);
//                CommitClass R = s.way.get(IdlastCommite);
//                String FileDir = R.Directory;
//                String MyDir = R.Directory + "\\" + "BEHKN.BEHKN";
//                try {
//                    temp = (ProjectToUpload) ResourceManager.load(MyDir);
//
//                } catch (Exception ex) {
//                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        }
        return temp;
    }

    /// compare two ProjectToUpload append to IdLastCommit
    private boolean compare_ProjectToUpload(ProjectToUpload server, ProjectToUpload client) {
        boolean ok = true;
        if (server.IdLastCommit != client.IdLastCommit) {
            return false;
        }
        return ok;
    }

    private void SendToGetPush(Command command) {
        String NameProject = ((GetPush) command).NameProject;
        /// ProjectToUpload from client
        ProjectToUpload clientFile = ((GetPush) command).getHiddenFile();
        /// important for path 
        String NameFolderSelect = ((GetPush) command).getNameFolderSelect();

        /// here must checking if Client Can Push The Project
        /// get ProjectToUpload from server
        ProjectToUpload serverFile = get_ProjectToUpload(clientFile.ProjectName, clientFile.BranchName);

        boolean ok = compare_ProjectToUpload(serverFile, clientFile);
        Respone respone;
        if (ok) {
            respone = new SendStatus(DONE);
            Send_Respone(respone);
        } else {
            /// here send FALIURE
            /// user dont have last change
            Send_FALIURE();
            /// Stop function
            return;
        }
        SendProject newRespone = null;
        try {
            newRespone = (SendProject) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {

        }
        ResourceManager.ShowViewfolder(newRespone.ob);

        Project serverproject = get_projectClass(clientFile.ProjectName);
        boolean Done = false;
        String NewCommitPlace = "";   /// String.valueOf(clientFile.IdLastCommit + 1);
        branchClass targetbranch = null;
        CommitClass CommitUserCreate = null;
        for (branchClass s : serverproject.branchListClass) {
            if (s.branchName.equals(clientFile.BranchName)) {
                /// get targetbranch to change last commit id , add new commit
                targetbranch = s;
                String CommentUser = ((GetPush) command).CommentUser;
                Done = true;
                /// add user commit to branch
                CommitUserCreate = s.addNewVersion(MyUser, CommentUser);
                if (CommitUserCreate == null) {
                    /// user is not of Contributors
                    Send_FALIURE();
                    /// Stop function
                    return;
                }
                /// get Directory for last commit that user add 
                int num = s.way.size() - 1;
                NewCommitPlace = s.way.get(num).Directory;

            }
        }

        if (!Done) {
            /// not fount branch
            Send_FALIURE();
            /// Stop function
            return;
        }
        /// Save Change in project Class 
        serverproject.Save();

        /// get directory to receive data from user
        File file = new File(NewCommitPlace);
        if (!file.exists()) {
            file.mkdir(); /// Create Folder that contain IdCommit
        }
//        File file2 = new File(NewCommitPlace + "\\" + NameFolderSelect);
//        file2.mkdir(); /// Create Folder that Contain NameOfFolderSelect that Client Select it for Push 

        CreateFolder(newRespone.ob, file.getPath(), NameFolderSelect);
        Receive(newRespone.ob, file.getPath(), NameFolderSelect);
        /// update_BENKH 

        System.out.println("Send BenkhFile");
        ProjectToUpload BenkhFile = get_ProjectToUpload(NameProject, clientFile.BranchName);
        try {
            //// Here we send hiddenFile to client
            System.out.println("Send BenkhFile:");
            output.writeObject(BenkhFile);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        Event_Class Ev = new Event_AddCommit(MyUser, NameProject, new Date(), CommitUserCreate);
        addNewEvent(Ev);
    }

    /// create folders in server for project 
    private void CreateFolder(ViewfolderClass ob, String NewDirectory, String NameFolderSelect) {
        List<NameAndDirectory> Folder = ob.MyFolder;

        for (NameAndDirectory u : Folder) {
            //// some problem in Directory between Server and Client
            String temp1 = u.Directory.substring(u.Directory.indexOf(NameFolderSelect));
            Path dir = Paths.get(temp1);
            dir = dir.subpath(1, dir.getNameCount());
            File folder = new File(NewDirectory + "\\" + dir);
            folder.mkdir();
            for (ViewfolderClass temp : ob.MyFolderView) {
                CreateFolder(temp, NewDirectory, NameFolderSelect);
            }
        }

    }

    /// Receive fils from client for new commit 
    private void Receive(ViewfolderClass ob, String NewDirectory, String NameFolderSelect) {
        for (NameAndDirectory temp : ob.MyFile) {
            FileOutputStream fos = null;
            try {
                String temp1 = temp.Directory.substring(temp.Directory.indexOf(NameFolderSelect));
                Path dir = Paths.get(temp1);
                dir = dir.subpath(1, dir.getNameCount());
                fos = new FileOutputStream(NewDirectory + "\\" + dir);
                SendFile respone;
                do {
                    respone = (SendFile) input.readObject();
                    //    fos.write(respone.getDataFile());
                    fos.write(respone.getDataFile(), 0, (int) Math.min(respone.getNumberOfByte(), 4096));
                } while (!respone.isEndOfFile());
                fos.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException | ClassNotFoundException ex) {
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                }
            }

        }
        for (ViewfolderClass temp : ob.MyFolderView) {
            Receive(temp, NewDirectory, NameFolderSelect);
        }
    }

    ///  send Contributors list to client
    private void SendToListContributors(Command command) {
        String NameProject = ((GetListContributors) command).getNameProject();

        Project project = get_projectClass(NameProject);
        CommonProject commonProject = Project_to_CommonProject(project);

        Respone respone = new SendListContributors(commonProject.Contributors);
        Send_Respone(respone);
    }

    ///  send Commits list to client
    private void SendToListCommits(Command command) {
        String NameProject = ((GetListCommits) command).getNameProject();
        String NameBranch = ((GetListCommits) command).getNameBranch();

        Project project = get_projectClass(NameProject);
        CommonProject commonproject = Project_to_CommonProject(project);
        int id = -1;
        int cnt = 0;
        for (branchClass s : project.branchListClass) {
            if (s.branchName.equals(NameBranch)) {
                id = cnt;
                break;
            }
            cnt++;
        }
        if (id == -1) {
            Send_FALIURE();
        }
        //// 0 is temp for NameBranch
        Respone respone = new SendListCommits(commonproject.BranchNames.get(id).way);
        Send_Respone(respone);

    }

    ///  send Branch list to client
    private void SendToListBranch(Command command) {
        String NameProject = ((GetListBranch) command).getNameProject();

        Project project = get_projectClass(NameProject);
        CommonProject commonProject = Project_to_CommonProject(project);

        Respone respone = new SendListBranch(commonProject.BranchNames);
        Send_Respone(respone);
    }

    private void SendToGetPull(Command command) {
        String NameProject = ((GetPull) command).NameProject;
        int idCommit = ((GetPull) command).getIdCommit();
        String BranchName = ((GetPull) command).getBranchName();
        /// get Directory for this commit 
        String dir = get_Directory_project(idCommit, BranchName, NameProject);
        ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
        SendProject Rc = new SendProject(ob);
        Send_Respone(Rc);
        SendFolder(ob);
        Project temp = get_projectClass(NameProject);
        ProjectToUpload BENHKFile = Get_BENKH(NameProject, BranchName, idCommit);
        try {
            output.writeObject(BENHKFile);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ProjectToUpload Get_BENKH(String NameProject, String BranchName, int idCommit) {
        ProjectToUpload temp = null;

        Project project = get_projectClass(NameProject);

        branchClass br = null;

        for (branchClass u : project.branchListClass) {
            if (u.branchName.equals(BranchName)) {
                br = u;
                break;
            }
        }

        temp = br.BENKH_File.get(idCommit - 1);

        return temp;
    }

    private void Send_Respone(Respone Rc) {
        try {
            output.writeObject(Rc);
            output.flush();
        } catch (IOException ex1) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return;
    }

    /// Send Folder
    private void SendFolder(ViewfolderClass ob) {
        for (NameAndDirectory temp : ob.MyFile) {
            GetFile get = new GetFile(temp.Directory);
            GETFILE(get);
        }
        for (ViewfolderClass temp : ob.MyFolderView) {
            SendFolder(temp);
        }
    }
    //// GET FILE

    private void GETFILE(Command command) {
        FileInputStream fis = null;
        try {
            byte[] DataFile = new byte[4096];
            String dir = ((GetFile) command).getDirectoryFile();
            File file = new File(dir);
            long size = file.length();
            long DateModified = file.lastModified();
            NameAndDirectory My = new NameAndDirectory(file.getName(), size, DateModified, dir);
            fis = new FileInputStream(file);
            long fileSize = file.length();

            int n;
            if (fileSize == 0) {
                Respone respone = new SendFile(DataFile, fileSize == 0, My, 0);
                output.writeObject(respone);
                output.flush();
            }
            while (fileSize > 0 && (n = fis.read(DataFile, 0, (int) Math.min(4096, fileSize))) != -1) {
                long tmp = fileSize;
                fileSize -= n;
                Respone respone = new SendFile(DataFile, fileSize == 0, My, tmp);
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

    /// Sign Up
    private void SendToSignUp(Command command) {
        try {
            boolean ok = SignUpClass.SignUp(((GetSIGNUP) command).user);
            if (ok) {
                MyUser = ((GetSIGNUP) command).user.getName();
                System.out.println("Ok");
                Send_Done();

            } else {
                Send_FALIURE();
            }
        } catch (IOException ex) {
            System.out.println("Error SIGNUP");
        }
    }
    // Login

    private void SendToLogin(Command command) {
        System.out.println("SendToLogin");
        boolean ok = LoginClass.Login(((GetLOGIN) command).user);
        if (ok) {
            MyUser = ((GetLOGIN) command).user.getName();
            Send_Done();
        } else {
            Send_FALIURE();
        }
    }

    /// creat project
    private void SendToStartProject(Command command) {
        boolean Access;
        String Author = MyUser;
        String ProjectDirectory = SeverBENKH.projectdirectoryName;
        String NameProject = ((GetStartProject) command).NameProject;
        String tempAccess = ((GetStartProject) command).Access;
        if ("true".equals(tempAccess)) {
            Access = true;
        } else {
            Access = false;
        }
        boolean ok = CanAddNewProjectToServer(NameProject);
        if (!ok) {
            Send_FALIURE();
            return;
        }
        Send_Done();
        Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);
        ProjectToUpload BenkhFile = get_ProjectToUpload(NameProject, "Master");
        //// Here we send hiddenFile to client 
        SendCreateProject Rc = new SendCreateProject(BenkhFile);
        Send_Respone(Rc);
    }

    /// to see if this project name is use before
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

    /// send list of my project to client
    private void SendToMyProject(Command command) {
        /// for Send To Client
        List< CommonProject> MyProject = GetMyProject();
        SendMyProject Rc = new SendMyProject(MyProject);
        Send_Respone(Rc);
    }

    /// send list of all project to client
    private void SendToAllProject(Command command) {
        /// for Send To Client
        List< CommonProject> AllProject = GetAllProject();
        SendAllProject Rc = new SendAllProject(DONE, AllProject);
        Send_Respone(Rc);
    }

    /// get list of project in server to help GetToAllProject and GetToMyProject
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

    private List< CommonProject> GetUserProject(String user) {
        List< CommonProject> MyProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (Contributor t : s.Contributors) {
                if (t.Name.equals(user)) {
                    ok = true;
                }
            }
            if (ok) {
                MyProject.add(Project_to_CommonProject(s));
            }
        }
        return MyProject;
    }

    private List< CommonProject> GetMyProject() {
        List< CommonProject> MyProject = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (Contributor t : s.Contributors) {
                if (t.Name.equals(MyUser)) {
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
            for (Contributor t : s.Contributors) {
                if (t.Name.equals(MyUser)) {
                    ok = true;
                }
            }
            if (ok || s.Access) {
                AllProject.add(Project_to_CommonProject(s));
            }
        }
        return AllProject;
    }

    /// get Public project in server 
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

    /// convert Project to CommonProject
    private CommonProject Project_to_CommonProject(Project MyProject) {
        CommonProject temp = new CommonProject();
        temp.Access = MyProject.Access;
        temp.Author = MyProject.Author;
        temp.Contributors = MyProject.Contributors;
        temp.NameProject = MyProject.NameProject;
        temp.id = MyProject.id;
        temp.numberOFBranshes = MyProject.numberOFBranshes;
        temp.DateCreate = MyProject.DateCreate;
        List< CommonBranch> BranchNames = new ArrayList<>();
        for (branchClass s : MyProject.branchListClass) {
            CommonBranch t = Branch_to_CommonBranch(s);
            BranchNames.add(t);
        }
        temp.BranchNames = BranchNames;
        return temp;
    }

    /// convert Branch to CommonBranch
    private CommonBranch Branch_to_CommonBranch(branchClass A) {
        CommonBranch R = null;
        Date lastCommite = A.lastCommite;
        String branchName = A.branchName;
        String userCreateBranch = A.userCreateBranch;
        List< CommitClass> way = A.way;
        R = new CommonBranch(lastCommite, branchName, userCreateBranch, way);
        return R;
    }

    /// get project first time
    private void GETPROJECT(Command command) {
        /// Get last Commit in master branch
        String NameProject = ((GetProject) command).NameProject;
        String dir = get_Directory_project_first_Time("Master", NameProject);
        if ("".equals(dir)) {
            Send_FALIURE();
        } else {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
            SendProject Rc = new SendProject(ob);
            Send_Respone(Rc);

        }
    }

    /// get branch in project 
    private void GetBranch(Command command) {

        /// Get last Commit in any branch
        String NameProject = ((GetBranch) command).NameProject;
        String branchName = ((GetBranch) command).BranchName;
        System.out.println(branchName + "Get ");
        String dir = get_Directory_project_first_Time(branchName, NameProject);
        if ("".equals(dir)) {
            Send_FALIURE();
        } else {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
            SendProject Rc = new SendBranch(ob);
            Send_Respone(Rc);
        }

    }

    private void Send_FALIURE() {
        Respone Rc = new SendStatus(ResponeType.FALIURE);
        try {
            output.writeObject(Rc);
            output.flush();
        } catch (IOException ex1) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private void Send_Done() {
        Respone Rc = new SendStatus(ResponeType.DONE);
        try {
            output.writeObject(Rc);
            output.flush();
        } catch (IOException ex1) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
    /// re send branch to update commits inside that branch

    private void GetCommits(Command command) {
        /// Get last Commit in any branch
        String NameProject = ((GetCommits) command).NameProject;
        String branchName = ((GetCommits) command).BranchName;
        int id = ((GetCommits) command).IDCommit;
        String dir = get_Directory_project(id, branchName, NameProject);
        if ("".equals(dir)) {
            Send_FALIURE();
        } else {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
            SendProject Rc = new SendBranch(ob);
            Send_Respone(Rc);
        }
    }

    /// get project class in server form info file 
    private Project get_projectClass(String temp) {
        try {
            return (Project) ResourceManager.load(projectdirectoryName + "\\" + temp + "\\" + "info");
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /// get Directory for select commit in branch in project 
    private String get_Directory_project(int idCommit, String BranchName, String NameProject) {
        String dir = "";
        Project myprojecProject = get_projectClass(NameProject);
        for (branchClass br : myprojecProject.branchListClass) {
            if (br.branchName.equals(BranchName)) /// get Branch
            {
                int id = idCommit - 1;
                dir = br.way.get(id).Directory;
            }
        }
        return dir;
    }

    /// get Directory for last commit in  selcet branch in project 
    private String get_Directory_project_first_Time(String BranchName, String NameProject) {
        String dir = "";
        Project myprojecProject = get_projectClass(NameProject);
        for (branchClass br : myprojecProject.branchListClass) {
            if (br.branchName.equals(BranchName)) /// get Branch
            {
                int sz = br.way.size() - 1;
                dir = br.way.get(sz).Directory;
            }
        }
        return dir;
    }

}
