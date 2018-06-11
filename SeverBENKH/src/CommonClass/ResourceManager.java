package CommonClass;

import CommonClass.NameAndDirectory;
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

    public static ViewDiff_folderClass ViewDiffProject(File CommitOne , File CommitTwo)
    {
        ViewDiff_folderClass MyViewdiff_folder = new ViewDiff_folderClass();
        //// need code
     return MyViewdiff_folder;   
    }
    /// this get list of files and folders inside folder
    public static ViewfolderClass ViewProject(File NameFolder) {

        ViewfolderClass MyViewfolder = new ViewfolderClass();
        String MyDirectory = NameFolder.getPath();
        for (File file : NameFolder.listFiles()) {

            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile();      // Check if it's a regular file
            if (isDirectory) {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New = new NameAndDirectory(Name, size, DateModified, Directory);
                MyViewfolder.MyFolder.add(New);
                File tempFolder = new File(MyDirectory + "\\" + file.getName());
                ViewfolderClass temp = ViewProject(tempFolder);
                MyViewfolder.MyFolderView.add(temp);

            } else {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New = new NameAndDirectory(Name, size, DateModified, Directory);
                MyViewfolder.MyFile.add(New);
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
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified=file.lastModified();
                NameAndDirectory New = new NameAndDirectory(Name,size,DateModified, Directory);
                MyViewfolder.MyFolder.add(New);
            } else {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified=file.lastModified();
                NameAndDirectory New = new NameAndDirectory(Name,size,DateModified, Directory);
                MyViewfolder.MyFile.add(New);
            }
        }
        return MyViewfolder;
    }

    /// this take Viewfile and Show it
    public static void ShowViewfolder(ViewfolderClass G) {
        for (NameAndDirectory f : G.MyFile) {
            System.out.println("File : " + f.Name + " " + f.Directory);
        }
        for (NameAndDirectory f : G.MyFolder) {
            System.out.println("File : " + f.Name + " " + f.Directory);
        }
    }
}
