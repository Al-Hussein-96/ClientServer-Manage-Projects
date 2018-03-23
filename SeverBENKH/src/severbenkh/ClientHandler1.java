package severbenkh;

import CommonClass.CommonProject;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.CommonServer.MyUser;

class ClientHandler1 extends Thread {

    DataOutputStream dos;
    FileInputStream fis;
    Socket client;

    ClientHandler1(Socket client) throws FileNotFoundException, IOException {
        this.client = client;
        dos = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
          SendToMyProject();
       // System.out.println("HELLO WORLD");

    }

    private void SendToMyProject() {
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

            dos = new DataOutputStream(client.getOutputStream());
            fis = new FileInputStream("temp.data");
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
