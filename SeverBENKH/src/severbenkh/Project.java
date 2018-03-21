package severbenkh;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Project implements Serializable {
    boolean Access = true;
    int id;
    String Author;
    String NameProject;
    String ProjectDirectory;
    /// Names of users work in project
    List<String> Contributors = new ArrayList<>();
    int numberOFBranshes;
    int NumberOfVersion;
    List< branchClass> branchListClass = new ArrayList<>();
    Date DateCreate  ;
    // add project to projects file
    /// don't make new project if ProjectDirectory is used from another project
    Project(boolean Access , String Author, String NameProject, String ProjectDirectory) {
        this.Access = Access;
        DateCreate = new Date();
        this.Author = Author;
        this.NameProject = NameProject;
        this.ProjectDirectory = ProjectDirectory;
        Contributors.add(Author);
        NumberOfVersion = 1;
        branchClass branchMaster = new branchClass(this, "Master" , Author);
        branchListClass.add(branchMaster);
        /// Creat Project Directory
        File CreateProjectDirectory = new File(ProjectDirectory);
        CreateProjectDirectory.mkdir();

        /// Creat Project File info
        String infoDirectory = ProjectDirectory + "\\" + "info";
        try {
            ResourceManager.save(this, infoDirectory);
        } catch (Exception ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    

}
