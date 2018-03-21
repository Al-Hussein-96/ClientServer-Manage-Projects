package severbenkh;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class branchClass  implements  Serializable{
    List < CommitClass > way = new ArrayList<>();
    String projectdirector ; 
    /// list of user download last Version
    List<String> UsersHowSeeLastUpdate = new ArrayList<>();
    Date lastCommite;
    Project father;
    String branchName ; 
    branchClass(Project father, String branchName , String Author)
    {
        this.branchName = branchName;
        lastCommite = new Date();
        this.father = father;
        this.projectdirector = father.ProjectDirectory ;
        int NumberOfVersion = father.NumberOfVersion;
        String Directory = projectdirector+"\\"+NumberOfVersion;
        CommitClass temp = new CommitClass(branchName , Author , Directory , "");
        way.add(temp);
        /// incres Number Of Version
        File CreateProjectDirectory= new File(projectdirector+"\\"+NumberOfVersion);
        CreateProjectDirectory.mkdir();
        UsersHowSeeLastUpdate.add(Author);
        father.NumberOfVersion++;
        
    }
    /// Create Branch have first num version from another brach
    branchClass(Project father, String branchName , branchClass fatherBranch , int num , String userCreateBranch)
    {
     this.branchName = branchName;
     this.father = father;
     this.projectdirector = father.ProjectDirectory ;
     for(int i=0;i<num;i++)
     {
        CommitClass temp = fatherBranch.way.get(i);
        temp.branchName = branchName;
        way.add(temp); 
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
    boolean addNewVersion(String Author , String Detail)
    {
        boolean Can = UserCanaddNewVersion(Author);
        if(!Can)
        {
            return false;
        }
       /// update Version
       UsersHowSeeLastUpdate.clear();
       UsersHowSeeLastUpdate.add(Author);
       int NumberOfVersion = father.NumberOfVersion;
       String Directory = projectdirector+"\\"+NumberOfVersion;
       CommitClass temp = new CommitClass(branchName , Author , Directory , Detail);
       way.add(temp);
       father.NumberOfVersion++;
       lastCommite = new Date();
       
       boolean ok_add = true;
        for(String s : father.Contributors)
        {
         if(s.equals(Author))
         {
             ok_add = false;
         }
        }
        if(ok_add)
        {
         father.Contributors.add(Author);
        }
       return true;
    }
    
    
    
}
