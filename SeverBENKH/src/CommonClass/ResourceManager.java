package CommonClass;

import CommonClass.NameAndDirectory;
import static CommonClass.StateType.*;
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

    public static ViewDiff_folderClass ViewDiffProject_del(File NameFolder) {
        ViewDiff_folderClass MyViewdiff_folder = new ViewDiff_folderClass();
        MyViewdiff_folder.MyState = Delete;
        String MyDirectory = NameFolder.getPath();
        for (File file : NameFolder.listFiles()) {

            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile();      // Check if it's a regular file
            if (isDirectory) {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory);
                NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Delete, null);

                MyViewdiff_folder.MyFolder.add(New);
                File tempFolder = new File(MyDirectory + "\\" + file.getName());
                ViewDiff_folderClass temp = ViewDiffProject_del(tempFolder);
                MyViewdiff_folder.MyFolderView.add(temp);

            } else {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory);
                NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Delete, null);
                MyViewdiff_folder.MyFile.add(New);
            }
        }
        return MyViewdiff_folder;
    }

    public static ViewDiff_folderClass ViewDiffProject_add(File NameFolder) {
        ViewDiff_folderClass MyViewdiff_folder = new ViewDiff_folderClass();
        MyViewdiff_folder.MyState = Add;
        String MyDirectory = NameFolder.getPath();
        for (File file : NameFolder.listFiles()) {

            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile();      // Check if it's a regular file
            if (isDirectory) {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory);
                NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Add, null);

                MyViewdiff_folder.MyFolder.add(New);
                File tempFolder = new File(MyDirectory + "\\" + file.getName());
                ViewDiff_folderClass temp = ViewDiffProject_add(tempFolder);
                MyViewdiff_folder.MyFolderView.add(temp);

            } else {
                String Directory = MyDirectory + "\\" + file.getName();
                String Name = file.getName();
                long size = file.length();
                long DateModified = file.lastModified();
                NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory);
                NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Add, null);
                MyViewdiff_folder.MyFile.add(New);
            }
        }
        return MyViewdiff_folder;
    }

    /// double bytes = file.length();
    public static ViewDiff_folderClass ViewDiffProject(File CommitOne, File CommitTwo) {
        System.out.println("CommitOne : " + CommitOne);
        System.out.println("CommitTwo : " + CommitTwo);
        ViewDiff_folderClass MyViewdiff_folder = new ViewDiff_folderClass();
        String DirectoryOne = CommitOne.getPath();
        String DirectoryTwo = CommitTwo.getPath();

        /// File Delete
        for (File file1 : CommitOne.listFiles()) {

            boolean isDirectoryOne = file1.isDirectory(); // Check if it's a directory
            boolean isFileOne = file1.isFile();      // Check if it's a regular file

            boolean isfound = false;
            for (File file : CommitTwo.listFiles()) {
                boolean isDirectoryTwo = file.isDirectory(); // Check if it's a directory
                boolean isFileTwo = file.isFile();      // Check if it's a regular file
                if (file.getName().equals(file1.getName())) {
                    if (isDirectoryOne && isDirectoryTwo) {
                        isfound = true;
                    } else if (isFileOne && isFileTwo) {
                        isfound = true;
                    }
                }
            }
            if (isfound == false) {
                MyViewdiff_folder.MyState = StateType.Change;
                if (isDirectoryOne) {
                    String Directory1 = DirectoryOne + "\\" + file1.getName();
                    String Name = file1.getName();
                    long size = file1.length();
                    long DateModified = file1.lastModified();
                    NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory1);
                    NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Delete, null);
                    File tempFolder1 = new File(Directory1);// + "\\" + file1.getName());
                    System.out.println("tempFolder1 : " + tempFolder1);
                    ViewDiff_folderClass temp = ViewDiffProject_del(tempFolder1);
                    New.MyState = temp.MyState;
                    MyViewdiff_folder.MyFolder.add(New);
                    MyViewdiff_folder.MyFolderView.add(temp);
                } else {
                    String Directory1 = DirectoryOne + "\\" + file1.getName();
                    String Name = file1.getName();
                    long size1 = file1.length();
                    long DateModified1 = file1.lastModified();
                    NameAndDirectory One = new NameAndDirectory(Name, size1, DateModified1, Directory1);
                    NameAndDirectoryAndState New = new NameAndDirectoryAndState(One, Delete, null);
                    MyViewdiff_folder.MyFile.add(New);
                }
            }
        }
        ////  Add & Change
        for (File file : CommitTwo.listFiles()) {

            boolean isDirectoryTwo = file.isDirectory(); // Check if it's a directory
            boolean isFileTwo = file.isFile();      // Check if it's a regular file

            boolean isfound = false;
            for (File file1 : CommitOne.listFiles()) {
                boolean isDirectoryOne = file1.isDirectory(); // Check if it's a directory
                boolean isFileOne = file1.isFile();      // Check if it's a regular file
                if (file.getName().equals(file1.getName())) {
                    /// file change or not
                    if (isDirectoryOne && isDirectoryTwo) {
                        isfound = true;
                        String Directory1 = DirectoryOne + "\\" + file1.getName();
                        String Directory2 = DirectoryTwo + "\\" + file.getName();

                        String Name = file.getName();

                        long size = file.length();
                        long DateModified = file.lastModified();

                        NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory2);
                        NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, NoChange, null);

                        File tempFolder1 = new File(Directory1);// + "\\" + file1.getName());
                        File tempFolder2 = new File(Directory2);// + "\\" + file.getName());
                        ViewDiff_folderClass temp = ViewDiffProject(tempFolder1, tempFolder2);
                        New.MyState = temp.MyState;
                        MyViewdiff_folder.MyFolder.add(New);
                        MyViewdiff_folder.MyFolderView.add(temp);
                        if(!New.MyState.equals(NoChange))
                        MyViewdiff_folder.MyState = Change;
                        
                    } else if (isFileOne && isFileTwo) {
                        isfound = true;
                        String Directory1 = DirectoryOne + "\\" + file1.getName();
                        String Directory2 = DirectoryTwo + "\\" + file.getName();

                        String Name = file.getName();
                        long size2 = file.length();
                        long size1 = file1.length();
                        long DateModified2 = file.lastModified();
                        long DateModified1 = file1.lastModified();
                        NameAndDirectory One = new NameAndDirectory(Name, size1, DateModified1, Directory1);
                        NameAndDirectory Two = new NameAndDirectory(Name, size2, DateModified2, Directory2);
                        if (size1 != size2) {
                            NameAndDirectoryAndState New = new NameAndDirectoryAndState(Two, Change, One);
                            MyViewdiff_folder.MyFile.add(New);
                            MyViewdiff_folder.MyState = Change;
                        } else {
                            NameAndDirectoryAndState New = new NameAndDirectoryAndState(Two, NoChange, null);
                            MyViewdiff_folder.MyFile.add(New);
                        }
                    }
                }
            }
            /// file add
            if (isfound == false) {
                MyViewdiff_folder.MyState = StateType.Change;
                if (isDirectoryTwo) {
                    String Directory2 = DirectoryTwo + "\\" + file.getName();
                    String Name = file.getName();
                    long size = file.length();
                    long DateModified = file.lastModified();
                    NameAndDirectory New1 = new NameAndDirectory(Name, size, DateModified, Directory2);
                    NameAndDirectoryAndState New = new NameAndDirectoryAndState(New1, Add, null);
                    File tempFolder2 = new File(Directory2 );//+ "\\" + file.getName());
                    ViewDiff_folderClass temp = ViewDiffProject_add(tempFolder2);
                    New.MyState = temp.MyState;
                    MyViewdiff_folder.MyFolder.add(New);
                    MyViewdiff_folder.MyFolderView.add(temp);
                } else {
                    String Directory2 = DirectoryTwo + "\\" + file.getName();
                    String Name = file.getName();
                    long size2 = file.length();
                    long DateModified2 = file.lastModified();
                    NameAndDirectory Two = new NameAndDirectory(Name, size2, DateModified2, Directory2);
                    NameAndDirectoryAndState New = new NameAndDirectoryAndState(Two, Add, null);
                    MyViewdiff_folder.MyFile.add(New);
                }
            }
        }

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
                long DateModified = file.lastModified();
                NameAndDirectory New = new NameAndDirectory(Name, size, DateModified, Directory);
                MyViewfolder.MyFolder.add(New);
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
