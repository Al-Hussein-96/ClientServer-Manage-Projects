package severbenkh;

import CommonClass.ViewfolderClass;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

    public static void save(Serializable data, String fileName) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(data);
        }
    }

    public static Object load(String fileName) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            return ois.readObject();
        }
    }

    /// this get list of files and folders inside folder
    public static ViewfolderClass ViewProject(File NameFolder) {
        ViewfolderClass MyViewfolder = new ViewfolderClass();
        String MyDirectory = NameFolder.getPath();
        for (File file : NameFolder.listFiles()) {
            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile();      // Check if it's a regular file
            if (isDirectory) {
                MyViewfolder.MyFolder.add(file.getName());
                File tempFolder = new File(MyDirectory + "\\" + file.getName());
                ViewfolderClass temp = ViewProject(tempFolder);
                MyViewfolder.MyFolderView.add(temp);
            } else {
                MyViewfolder.MyFile.add( file.getName());
            }
        }
        return MyViewfolder;
    }


    /// this take Viewfile and Show it
    public static void ShowViewfolder(ViewfolderClass G ) {
        for (String f : G.MyFile) {
            System.out.println("File : " + f);
        }
        for (String f : G.MyFolder) {
            System.out.println("Folder : " + f);
        }
           
    }
    
    public static void ShowViewProject(ViewfolderClass G ) {
        for (String f : G.MyFile) {
            System.out.println("File : " + f);
        }
        int cnt = 0;
        for (String f : G.MyFolder) {
            System.out.println("Folder : " + f);
            ShowViewProject(G.MyFolderView.get(cnt));
            cnt++;
        }
           
    }
}
