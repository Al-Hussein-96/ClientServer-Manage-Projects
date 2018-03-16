package severbenkh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverBENKH {

    private static ServerSocket serverSocket;
    private static final int PORT = 4321;

    public static String idFileName = "src\\id.Name";
    public static String idFileProject = "src\\id.Project";
    public static String projectdirectoryName = "src\\Projects Information";
    public static String usersdirectoryName = "src\\Users Information";
    
  ///  public static String ProjectFileName = projectdirectoryName + "Projects.data";

    public static void main(String[] args) throws IOException {

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

        FileInputStream id = new FileInputStream(idFilename);
        int x = id.read();
        id.close();
        FileOutputStream idd = new FileOutputStream(idFilename);
        x++;
        idd.write(x);
        x--;
        return x;
    }
    
     public static int idincreProject(String idFileProject) throws FileNotFoundException, IOException {

        FileInputStream id = new FileInputStream(idFileProject);
        int x = id.read();
        id.close();
        FileOutputStream idd = new FileOutputStream(idFileProject);
        x++;
        idd.write(x);
        x--;
        return x;
    }

    private static void initFile() throws FileNotFoundException, IOException {

        // create id Name
        File idFile = new File(idFileName);
        if (!idFile.exists()) {
            FileOutputStream id = new FileOutputStream(idFile);
            System.out.print(0);
            id.write(0);
            id.close();
        }
        // create id project Name
        File idFileproject = new File(idFileProject);
        if (!idFileproject.exists()) {
            FileOutputStream id = new FileOutputStream(idFileproject);
            System.out.print(0);
            id.write(0);
            id.close();
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
    }
    
    private static boolean ProjectNameAllow(String ProjectName) {

        File idFile = new File(projectdirectoryName +"\\" + ProjectName);
        if (!idFile.exists()) {
            return true;
        }
        return false;
    }
    
    private static boolean UserNameAllow(String UserName) {

        File idFile = new File(usersdirectoryName +"\\" + UserName);
        if (!idFile.exists()) {
            return true;
        }
        return false;
    }
    

}
