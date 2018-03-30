package CommonClass;

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
                MyViewfolder.MyFolder.add(MyDirectory + "\\" + file.getName());
                File tempFolder = new File(MyDirectory + "\\" + file.getName());
                ViewfolderClass temp = ViewProject(tempFolder);
                for (String s : temp.MyFile) {
                    MyViewfolder.MyFile.add(s);
                }
                for (String s : temp.MyFolder) {
                    MyViewfolder.MyFolder.add(s);
                }
            } else {
                MyViewfolder.MyFile.add(MyDirectory + "\\" + file.getName());
            }
        }
        return MyViewfolder;
    }

    /// this get list of files and folders inside folder
    public static ViewfolderClass ViewFolder(File NameFolder) {
        ViewfolderClass MyViewfolder = new ViewfolderClass();
        String MyDirectory = NameFolder.getPath();
        for (File file : NameFolder.listFiles()) {
            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile();      // Check if it's a regular file
            if (isDirectory) {
                MyViewfolder.MyFolder.add(MyDirectory + "\\" + file.getName());
            } else {
                MyViewfolder.MyFile.add(MyDirectory + "\\" + file.getName());
            }
        }
        return MyViewfolder;
    }

    /// this take Viewfile and Show it
    public static void ShowViewfolder(ViewfolderClass G) {
        for (String f : G.MyFile) {
            System.out.println("File : " + f);
        }
        for (String f : G.MyFolder) {
            System.out.println("Folder : " + f);
        }
    }
}
