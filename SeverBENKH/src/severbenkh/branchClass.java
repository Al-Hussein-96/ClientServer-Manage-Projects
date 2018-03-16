package severbenkh;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class branchClass {
    List < String > way = new ArrayList<>();
    String projectdirector ; 
    
    Date lastCommite;
    branchClass(Project father, String branchName)
    {
        this.projectdirector = father.ProjectDirectory ;
        int NumberOfVersion = father.NumberOfVersion;
        way.add(projectdirector+"\\"+NumberOfVersion);
        /// incres Number Of Version
        father.NumberOfVersion++;
        File CreateProjectDirectory= new File(projectdirector+"\\"+"1");
        CreateProjectDirectory.mkdir();
        
    }
    
    
}
