package severbenkh;

import CommonClass.Profile;
import CommonClass.ResourceManager;
import CommonClass.User;
import CommonClass.ViewfolderClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeverBENKH {

    private static ServerSocket serverSocket;
    private static final int PORT = 4321;

    public static String idFileName = "Data\\id.Name";
    public static String idFileProject = "Data\\id.Project";
    public static String projectdirectoryName = "Data\\Projects Information";
    public static String usersdirectoryName = "Data\\Users Information";
    public static String list_user_in_server = "Data\\Users Information\\All-User.data";
    public static void main(String[] args) throws IOException {

        ViewfolderClass vi = ResourceManager.ViewProject(new File("src"));
        ResourceManager.ShowViewfolder(vi);
        initFile();

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            System.out.println("Unable to Connect to Port");
            System.exit(1);
        }
        do {
            Socket client = serverSocket.accept();
            System.out.println("New Client Accepted");

            ClientHandler handler = new ClientHandler(client);
            handler.start();
        } while (true);

    }

    public static int idincreUser(String idFilename) throws FileNotFoundException, IOException {
        int x;
        try (FileInputStream id = new FileInputStream(idFilename)) {
            x = id.read();
        }
        FileOutputStream idd = new FileOutputStream(idFilename);
        x++;
        idd.write(x);
        x--;
        return x;
    }

    public static int idincreProject(String idFileProject) throws FileNotFoundException, IOException {

        int x;
        try (FileInputStream id = new FileInputStream(idFileProject)) {
            x = id.read();
        }
        FileOutputStream idd = new FileOutputStream(idFileProject);
        x++;
        idd.write(x);
        x--;
        return x;
    }

    private static void initFile() throws FileNotFoundException, IOException {

        
        File DataFile = new File("Data");
        if (!DataFile.exists()) {
            DataFile.mkdir();
        }
        // create id Name
        File idFile = new File(idFileName);
        if (!idFile.exists()) {
            try (FileOutputStream id = new FileOutputStream(idFile)) {
                id.write(0);
            }
        }
        // create id project Name
        File idFileproject = new File(idFileProject);
        if (!idFileproject.exists()) {
            try (FileOutputStream id = new FileOutputStream(idFileproject)) {
                id.write(0);
            }
        }

        // create users directory 
        File userDir = new File(usersdirectoryName);
        if (!userDir.exists()) {
            userDir.mkdir();

        }

        // create projects directory
        File projectDir = new File(projectdirectoryName);
        if (!projectDir.exists()) {
            projectDir.mkdir();

        }
        File temp = new File(list_user_in_server);
        if(!temp.exists())
        {
            List<User> t = new ArrayList<>();
            try {
                ResourceManager.save((Serializable) t, list_user_in_server);
            } catch (Exception ex) {
                Logger.getLogger(SeverBENKH.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private static boolean ProjectNameAllow(String ProjectName) {

        File idFile = new File(projectdirectoryName + "\\" + ProjectName);
        return !idFile.exists();
    }

    private static boolean UserNameAllow(String UserName) {

        File idFile = new File(usersdirectoryName + "\\" + UserName);
        return !idFile.exists();
    }

}
