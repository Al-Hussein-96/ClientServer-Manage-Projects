package severbenkh;

import CommonClass.ResourceManager;
import CommonClass.Contributor;
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
    List< Contributor > Contributors = new ArrayList<>();
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
        this.ProjectDirectory = ProjectDirectory+"\\"+NameProject;     
        /// Creat Project Directory
        File CreateProjectDirectory = new File(this.ProjectDirectory );
        CreateProjectDirectory.mkdir();
        
        Contributors.add(new Contributor(Author));
        NumberOfVersion = 1;
        branchClass branchMaster = new branchClass(this, "Master" , Author);
        branchListClass.add(branchMaster);
        
        /// Creat Project File info
        String infoDirectory = this.ProjectDirectory + "\\" + "info";
        try {
            ResourceManager.save(this, infoDirectory);
        } catch (Exception ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    

}
