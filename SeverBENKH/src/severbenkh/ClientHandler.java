package severbenkh;

import CommonClass.ResourceManager;
import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import CommonClass.ViewfolderClass;
import CommonClass.CommonProject;
import CommonClass.Contributor;
import CommonClass.NameAndDirectory;
import CommonClass.NameAndDirectoryAndState;
import CommonClass.Profile;
import CommonClass.ProjectToUpload;
import static CommonClass.StateType.NoChange;
import CommonClass.User;
import CommonClass.ViewDiff_folderClass;
import CommonCommand.*;
import CommonRespone.*;
import static CommonRespone.ResponeType.DONE;
import Different.*;
import EventClass.Event_AddBranch;
import EventClass.Event_AddCommit;
import EventClass.Event_AddContributor;
import EventClass.Event_Class;
import Merge.Merge;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
                System.out.println("Cann't Read Command: " + ex.getMessage());
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
                case GetDiffrent:
                    SendToGetDiffrent(command);
                    break;
                case GetDiffFile:
                    SendToGetDiffFile(command);
                    break;

                case GetNewEvent:
                    SendToGetNewEvent(command);
                    break;
                case GETMERGE:
                    SendToGetMerge(command);
                    break;
                case Follow_Project:
                    SendToFollow(command);
                    break;
                case MyFollowProjects:
                    SendToGetMyFollowProjects(command);
                    break;
                case GetPullAndMerge:
                    SendToGetPullAndMerge(command);
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

    private ProjectToUpload add_User_And_Password(ProjectToUpload temp) {
        User user = get_information(MyUser);
        temp.add_Local_User(user);
        return temp;
    }

    private void SendToGetMyFollowProjects(Command command) {
        List<String> MyFollowProjects = get_MyFollow_Project(MyUser);
        Respone res = new SendMyFollowProjects(MyFollowProjects);
        Send_Respone(res);

    }

    private String get_Base(String NameProject, String BranchFirst, String BranchSecond) {
        String dir = null;
        int last1 = 1, last2 = 1;

        while (true) {
            branchClass br1 = get_BranchClass_in_Project(NameProject, BranchFirst);
            branchClass br2 = get_BranchClass_in_Project(NameProject, BranchSecond);
            if (br1.branchName.equals(br2.branchName)) {
                int X = last1;
                if (last2 > X) {
                    X = last2;
                }
                dir = get_Directory_project(X, br1.branchName, NameProject);
                break;
            }
            last1 = br1.CommitFather;
            last2 = br2.CommitFather;
            if (br1.BranchLevel > br2.BranchLevel) {
                BranchFirst = br1.BranchFather;
            } else {
                BranchSecond = br2.BranchFather;
            }

        }
        return dir;
    }

    private void SendToGetPullAndMerge(Command command) {

        /// first make push on temp File 
        String NameProject = ((GetPush) command).NameProject;
        ProjectToUpload clientFile = ((GetPush) command).getHiddenFile();
        /// important for path 
        String NameFolderSelect = ((GetPush) command).getNameFolderSelect();

        ProjectToUpload serverFile = get_ProjectToUpload(clientFile.ProjectName, clientFile.BranchName);

        String Base = get_Directory_project(clientFile.IdLastCommit, clientFile.BranchName, clientFile.ProjectName);
        String dir1 = get_Directory_project_first_Time(clientFile.BranchName, clientFile.ProjectName);
        String dir2 = null;

        SendProject newRespone = null;
        try {
            newRespone = (SendProject) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {

        }
        ResourceManager.ShowViewfolder(newRespone.ob);

        Project serverproject = get_projectClass(clientFile.ProjectName);
        String NewTempCommitPlace = "";   /// temp File Save
        int idTempFile = 1;
        try {
            idTempFile = SeverBENKH.idTempFileIncre();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewTempCommitPlace = SeverBENKH.TempFiledirectory + "\\" + idTempFile;
        dir2 = NewTempCommitPlace;
        /// get directory to receive data from user
        File file = new File(NewTempCommitPlace);
        if (!file.exists()) {
            file.mkdir(); /// Create Folder that contain IdCommit
        }

        CreateFolder(newRespone.ob, file.getPath(), NameFolderSelect);
        Receive(newRespone.ob, file.getPath(), NameFolderSelect);

        /// Here I resive data and need compare 
        ViewDiff_folderClass ob = ResourceManager.ViewDiffProject(new File(dir1), new File(dir2));

        SendProject_Merge Rc = new SendProject_Merge(ob);
        Send_Respone(Rc);
        SendFolder(ob, Base);

        ProjectToUpload BENHKFile = get_ProjectToUpload(NameProject, clientFile.BranchName);
        BENHKFile = add_User_And_Password(BENHKFile);

        try {
            output.writeObject(BENHKFile);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        /// we should delete Temp File
    }

    private void SendToGetMerge(Command command) {
        //// here must be Merge Files
        String NameProject = ((GetMerge) command).getNameProject();
        String BranchFirst = ((GetMerge) command).getBranchFirst();
        String BranchSecond = ((GetMerge) command).getBranchSecond();
        /// BranchFirst = BranchSecond need code
        String dir1 = get_Directory_project_first_Time(BranchFirst, NameProject);
        String dir2 = get_Directory_project_first_Time(BranchSecond, NameProject);
        String Base = get_Base(NameProject, BranchFirst, BranchSecond);

        ViewDiff_folderClass ob = ResourceManager.ViewDiffProject(new File(dir1), new File(dir2));

        SendProject_Merge Rc = new SendProject_Merge(ob);
        Send_Respone(Rc);
        SendFolder(ob, Base);

        ProjectToUpload BENHKFile = get_ProjectToUpload(NameProject, BranchFirst);
        BENHKFile = add_User_And_Password(BENHKFile);

        try {
            output.writeObject(BENHKFile);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void SendToGetNewEvent(Command command) {
        List<Event_Class> NewEvent = get_NewEvent();


        Respone res = new SendNewEvent(NewEvent);
        Send_Respone(res);
    }

    /**
     * here must get Two File and Find Different , Fill List and Send it To
     * Client DirFile1 : Directory Of File1 On Server DirFile2 : Directory Of
     * File2 On Server
     *
     * @param command
     */
    private void SendToGetDiffFile(Command command) {

        String Dir1 = ((GetDiffFile) command).DirFile1;
        String Dir2 = ((GetDiffFile) command).DirFile2;
        Myers01 Myers = new Myers01(Dir1, Dir2);
        Diff Difference = Myers.getDiff();

        for (Changes change : Difference.getChanges()) {
//            if (change instanceof Insert) {
//                System.out.print("Insert : ");
//            }
//            if (change instanceof Delete) {
//                System.out.print("Delete : ");
//            }
//            if (change instanceof NoChange) {
//                System.out.print("NoChange : ");
//            }
//            if ("".equals(change.getObject().trim())) {
//                System.out.println("NEW LINE");
//            } else {
//                System.out.println(change.getObject());
//            }
        }
        /**
         * Difference is List<Changes> where Changes is abstract class and have
         * three child (Delete , Insert , NoChange) where everyone is String
         * equals to the line
         *
         */
        SendDiffFile send = new SendDiffFile(Difference);
        Send_Respone(send);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void set_list_user_in_server(List< User> temp) {
        try {
            ResourceManager.save((Serializable) temp, list_user_in_server);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List< User> get_list_user_in_server() {
        List< User> temp = null;
        try {
            temp = (List< User>) ResourceManager.load(list_user_in_server);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    private void SendToListUsers(Command command) {
        try {
            List< User> temp = get_list_user_in_server();
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
        List<String> MyFollow = get_MyFollow_Project(user);
        List< CommonProject> MyProjectFollow = new ArrayList<>();
        List< Project> TempList = getAllProjectInServer();
        for (Project s : TempList) {
            boolean ok = false;
            for (String t : MyFollow) {
                if (t.equals(s.NameProject)) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                MyProjectFollow.add(Project_to_CommonProject(s));
            }
        }
        temp = new Profile(NewUser, OwnProject, ContributorProject,MyProjectFollow);
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
            Send_FALIURE("You don't have permission to add Contributor");
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
        Send_FALIURE("there is no users in server");
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
            Send_FALIURE("You don't have permission to add Branch");
            return;
        }
        for (branchClass s : Myproject.branchListClass) {
            if (s.branchName.equals(BranchName)) {
                /// there is branch same name 
                Send_FALIURE("Branch Name is used before try another name");
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

    private void SendToFollow(Command command) {
        String ProjectName = ((FollowProject) command).ProjectName;
        boolean isFollow = ((FollowProject) command).Follow;
        if (isFollow) {
            add_follow_to_User(MyUser, ProjectName);
        } else {
            delete_follow_to_User(MyUser, ProjectName);
        }
        Send_Done();
    }

    private void add_follow_to_User(String user, String Project) {
        List< User> ServerUser = get_list_user_in_server();
        for (User temp : ServerUser) {
            if (temp.getName().equals(user)) {
                temp.add_Follow(Project);
            }
        }
        set_list_user_in_server(ServerUser);
    }

    private void delete_follow_to_User(String user, String Project) {
        List< User> ServerUser = get_list_user_in_server();
        for (User temp : ServerUser) {
            if (temp.getName().equals(user)) {
                temp.delete_Follow(Project);
            }
        }
        set_list_user_in_server(ServerUser);
    }

    private Event_Class get_Event(int id) {
        Event_Class Ev = null;
        String dir = SeverBENKH.EventDirectory + "\\" + id;
        try {
            Ev = (Event_Class) ResourceManager.load(dir);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Ev;
    }

    private List< Event_Class> get_NewEvent() {
        String user = MyUser;
        List< User> T = get_list_user_in_server();
        int Id_last_Event = 0;
        for (User s : T) {
            if (s.getName().equals(user)) {
                Id_last_Event = s.get_Last_Event_See();
                break;
            }
        }
        int lastEvent = 0;
        try {
            lastEvent = SeverBENKH.getIdLastEvent();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        List< String> MyFollowProject = get_MyFollow_Project(user);
        List< Event_Class> MyEvent = new ArrayList<>();
        for (int i = Id_last_Event + 1; i < lastEvent; i++) {
            Event_Class Ev = get_Event(i);
            for (String s : MyFollowProject) {
                if (s.equals(Ev.ProjectName)) {
                    MyEvent.add(Ev);
                    break;
                }
            }
        }
        Update_User_Last_Event_See(user, lastEvent - 1);
        return MyEvent;
    }

    private void Update_User_Last_Event_See(String user, int id) {
        List< User> ServerUser = get_list_user_in_server();
        for (User temp : ServerUser) {
            if (temp.getName().equals(user)) {
                temp.Update_Last_Event_See(id);
            }
        }
        set_list_user_in_server(ServerUser);
    }

    private List< String> get_MyFollow_Project(String user) {
        List< User> ServerUser = get_list_user_in_server();
        for (User temp : ServerUser) {
            if (temp.getName().equals(user)) {
                return temp.getMyFollow();
            }
        }
        return null;
    }

    private void addNewEvent(Event_Class Ev) {
        try {
            int Id = SeverBENKH.idincreEvent();
            String dir = SeverBENKH.EventDirectory + "\\" + Id;
            ResourceManager.save(Ev, dir);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private NameAndDirectoryAndState NameAndDirectoryAndState_to_NameAndDirectory(NameAndDirectory temp) {
        NameAndDirectoryAndState My = null;
        My.MyState = NoChange;
        My.MyFile = temp;
        return My;
    }

    private ViewDiff_folderClass ViewfolderClass_to_ViewDiff_folderClass(ViewfolderClass temp) {
        ViewDiff_folderClass My = new ViewDiff_folderClass();
        /// NameAndDirectoryAndState
        for (NameAndDirectory R : temp.MyFile) {
            NameAndDirectoryAndState T;
            T = NameAndDirectoryAndState_to_NameAndDirectory(R);
            My.MyFile.add(T);
        }
        for (NameAndDirectory R : temp.MyFolder) {
            NameAndDirectoryAndState T;
            T = NameAndDirectoryAndState_to_NameAndDirectory(R);
            My.MyFolder.add(T);
        }

        for (ViewfolderClass R : temp.MyFolderView) {
            ViewDiff_folderClass T;
            T = ViewfolderClass_to_ViewDiff_folderClass(R);
            My.MyFolderView.add(T);
        }
        return My;
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
            Send_FALIURE("You don't have the last version try pull and Merge");
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
                    Send_FALIURE("You don't have permission to make push because you are not Contributors in this project");
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
            Send_FALIURE("Branch Not found");
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

        ProjectToUpload BenkhFile = get_ProjectToUpload(NameProject, clientFile.BranchName);
        BenkhFile = add_User_And_Password(BenkhFile);
        try {
            //// Here we send hiddenFile to client
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
            Send_FALIURE("Branch not found");
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
        BENHKFile = add_User_And_Password(BENHKFile);
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

    /// Send Folder for merge
    private void SendFolder(ViewDiff_folderClass ob, String Base) {
        for (NameAndDirectoryAndState temp : ob.MyFile) {
            //// if there is old file then  (merge file need )

            /// Here we Have three directory 
            /// 1 - Base  = Base+"\\"+temp.MyFile.Name
            /// 2 - first = temp.MyFile.Directory;
            /// 3 - secound = temp.OldFile.Directory;
            if (temp.OldFile == null) {
                GetFile get = new GetFile(temp.MyFile.Directory);
                GETFILE(get);
            } else {
                String dir1 = Base + "\\" + temp.MyFile.Name;
                String dir2 = temp.MyFile.Directory;
                String dir3 = dir3 = temp.OldFile.Directory;
                File base = new File(dir1);
                File filenew = new File(dir2);
                File fileold = new File(dir3);
                if (base.exists() && fileold.exists() && filenew.exists()) {
                    Merge M = new Merge(dir1, dir2, dir3);
                    List<String> MergingList = M.getMergingList();
                    //this List most send as Merging to the filenew and fileold
                    M.write(MergingList, new File(SeverBENKH.tempFile));
                    GetFile get = new GetFile(SeverBENKH.tempFile);
                    GETFILE(get);

                } else if (fileold.exists() && filenew.exists()) {
                    //her make the base file as empty file ... 
                    Merge M = new Merge(SeverBENKH.emptyFile, dir2, dir3);
                    List<String> MergingList = M.getMergingList();
                    M.write(MergingList, new File(SeverBENKH.tempFile));
                    GetFile get = new GetFile(SeverBENKH.tempFile);
                    GETFILE(get);

                } else if (filenew.exists()) {
                    GetFile get = new GetFile(temp.MyFile.Directory);
                    GETFILE(get);
                }
            }

        }
        int cnt = 0;
        for (ViewDiff_folderClass temp : ob.MyFolderView) {
            String F = ob.MyFolder.get(cnt).MyFile.Name;
            SendFolder(temp, Base + "\\" + F);
            cnt++;
        }
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
        
                Send_Done();

            } else {
                Send_FALIURE("User name used by another person try another name");
            }
        } catch (IOException ex) {
            System.out.println("Error SIGNUP");
        }
    }
    // Login

    private void SendToLogin(Command command) {
      
        boolean ok = LoginClass.Login(((GetLOGIN) command).user);
        if (ok) {
            MyUser = ((GetLOGIN) command).user.getName();
            Send_Done();
        } else {
            Send_FALIURE("incorrect username or password");
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
            Send_FALIURE("Project name used by another person try another name");
            return;
        }
        Send_Done();
        Project NewProject = new Project(Access, Author, NameProject, ProjectDirectory);
        ProjectToUpload BenkhFile = get_ProjectToUpload(NameProject, "Master");
        BenkhFile = add_User_And_Password(BenkhFile);
        //// Here we send hiddenFile to client 

        SendCreateProject Rc = new SendCreateProject(BenkhFile);
        Send_Respone(Rc);

        /// Her i set the new project in My follow and must add to every contribut add
        add_follow_to_User(MyUser, NameProject);
        //
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
//        for (CommonProject CP : MyProject) {
//            System.out.println(CP.NameProject);
//        }
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
            Send_FALIURE("Project not found");
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
        
        String dir = get_Directory_project_first_Time(branchName, NameProject);
        if ("".equals(dir)) {
            Send_FALIURE("Branch not found");
        } else {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
            SendProject Rc = new SendBranch(ob);
            Send_Respone(Rc);
        }

    }

    private void Send_FALIURE(String Message) {
        Respone Rc = new SendStatus(ResponeType.FALIURE);
        Rc.Message = Message;
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
            Send_FALIURE("Commit not found");
        } else {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(dir));
            SendProject Rc = new SendBranch(ob);
            Send_Respone(Rc);
        }
    }

    private void SendToGetDiffrent(Command command) {
        String NameProject = ((Get_Diff_Two_Commit) command).NameProject;
        String branchName = ((Get_Diff_Two_Commit) command).BranchName;
        int IDCommitOne = ((Get_Diff_Two_Commit) command).IDCommitOne;
        int IDCommitTwo = ((Get_Diff_Two_Commit) command).IDCommitTwo;
        String dir1 = get_Directory_project(IDCommitOne, branchName, NameProject);
        String dir2 = get_Directory_project(IDCommitTwo, branchName, NameProject);

        if ("".equals(dir1)) {
            Send_FALIURE("first commit not found");
        } else if ("".equals(dir2)) {
            Send_FALIURE("second commit not found");
        } else {
            ViewDiff_folderClass ob = ResourceManager.ViewDiffProject(new File(dir1), new File(dir2));
            Send_Diff_Two_Commit Rc = new Send_Diff_Two_Commit(ob);
            Send_Respone(Rc);
        }
    }

    private branchClass get_BranchClass_in_Project(String NameProject, String BranchName) {
        Project Pr = get_projectClass(NameProject);
        for (branchClass temp : Pr.branchListClass) {
            if (temp.branchName.equals(BranchName)) {
                return temp;
            }

        }
        return null;
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
