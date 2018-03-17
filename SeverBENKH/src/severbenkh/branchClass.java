package severbenkh;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class branchClass  implements  Serializable{
    List < String > way = new ArrayList<>();
    String projectdirector ; 
    /// list of user download last Version
    List<String> users = new ArrayList<>();
    Date lastCommite;
    Project father;
    branchClass(Project father, String branchName , String userCreateBranch)
    {
        this.father = father;
        this.projectdirector = father.ProjectDirectory ;
        int NumberOfVersion = father.NumberOfVersion;
        way.add(projectdirector+"\\"+NumberOfVersion);
        /// incres Number Of Version
        File CreateProjectDirectory= new File(projectdirector+"\\"+NumberOfVersion);
        CreateProjectDirectory.mkdir();
        users.add(userCreateBranch);
        father.NumberOfVersion++;
        
    }
    /// Create Branch have first num version from another brach
    branchClass(Project father, String branchName , branchClass fatherBranch , int num , String userCreateBranch)
    {
     this.father = father;
     this.projectdirector = father.ProjectDirectory ;
     for(int i=0;i<num;i++)
     {
        way.add(fatherBranch.way.get(i)); 
     }
     users.add(userCreateBranch);
    }
    
    boolean UserCanaddNewVersion(String user)
    {
        for(String temp : users)
        {
            if(temp.equals(user))
            {
                return true;
            }
        }
        return false;
    }
    
    /// not complet
    boolean addNewVersion(String user)
    {
       /// update Version
       users.clear();
       users.add(user);
       int NumberOfVersion = father.NumberOfVersion;
       way.add(projectdirector+"\\"+NumberOfVersion);
       father.NumberOfVersion++;
       
       return true;
    }
    
    
    
}
