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
    List<String> UsersHowSeeLastUpdate = new ArrayList<>();
    Date lastCommite;
    Project father;
    private void ContributorIsFoundOrNot(String Contributor)
    {
        
    }
    branchClass(Project father, String branchName , String Author)
    {
        lastCommite = new Date();
        this.father = father;
        this.projectdirector = father.ProjectDirectory ;
        int NumberOfVersion = father.NumberOfVersion;
        way.add(projectdirector+"\\"+NumberOfVersion);
        /// incres Number Of Version
        File CreateProjectDirectory= new File(projectdirector+"\\"+NumberOfVersion);
        CreateProjectDirectory.mkdir();
        UsersHowSeeLastUpdate.add(Author);
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
     UsersHowSeeLastUpdate.add(userCreateBranch);
     lastCommite = new Date();
     boolean ok_add = true;
     for(String s : father.Contributors)
     {
      if(s.equals(userCreateBranch))
      {
          ok_add = false;
      }
     }
     if(ok_add)
     {
      father.Contributors.add(userCreateBranch);
     }
    }
    
    boolean UserCanaddNewVersion(String user)
    {
        for(String temp : UsersHowSeeLastUpdate)
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
       UsersHowSeeLastUpdate.clear();
       UsersHowSeeLastUpdate.add(user);
       int NumberOfVersion = father.NumberOfVersion;
       way.add(projectdirector+"\\"+NumberOfVersion);
       father.NumberOfVersion++;
       lastCommite = new Date();
       
       boolean ok_add = true;
        for(String s : father.Contributors)
        {
         if(s.equals(user))
         {
             ok_add = false;
         }
        }
        if(ok_add)
        {
         father.Contributors.add(user);
        }
       return true;
    }
    
    
    
}
