package severbenkh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Project  implements Serializable{
    int id;
    String userCreateProject;
    String ProjectName;
    String ProjectDirectory;
    /// Names of users work in project
    List<String>users = new ArrayList<>();
    int numberOFBranshes;
    int NumberOfVersion;
    List < branchClass > branchListClass = new ArrayList<>();
    
    // add project to projects file
    /// don't make new project if ProjectDirectory is used from another project

    Project(String userCreateProject , String ProjectName , String ProjectDirectory) throws IOException, Exception{
        this.userCreateProject = userCreateProject;
        this.ProjectName = ProjectName;
        this.ProjectDirectory = ProjectDirectory;
        users.add(userCreateProject);
        NumberOfVersion = 1;
        numberOFBranshes = 1;
        branchClass branchMaster = new branchClass(this , "Master");
        branchListClass.add(branchMaster);
        
        /// Creat Project Directory
        File CreateProjectDirectory= new File(ProjectDirectory);
        CreateProjectDirectory.mkdir();
        
        /// Creat Project File info
        String infoDirectory = ProjectDirectory+"\\"+"info";
        ResourceManager.save(this , infoDirectory);

    }
    
    
}
