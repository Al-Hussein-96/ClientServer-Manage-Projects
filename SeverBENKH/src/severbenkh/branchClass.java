package severbenkh;

import CommonClass.CommitClass;
import CommonClass.Contributor;
import CommonClass.ProjectToUpload;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class branchClass implements Serializable {

    List< CommitClass> way = new ArrayList<>();
    String projectdirector;
    /// list of user download last Version
    List<String> UsersHowSeeLastUpdate = new ArrayList<>();
    Date lastCommite;
    Project father;
    String branchName;
    String userCreateBranch ; 
    branchClass(Project father, String branchName, String Author) {
        this.userCreateBranch = Author ; 
        this.branchName = branchName;
        lastCommite = new Date();
        this.father = father;
        this.projectdirector = father.ProjectDirectory;
        int NumberOfVersion = father.NumberOfVersion;
        System.out.println("Here Error : ");
        System.out.println(projectdirector);
        String Directory = this.projectdirector + "\\" + NumberOfVersion;
        System.out.println(Directory);
        CommitClass temp = new CommitClass(branchName, Author, Directory, "", 1);
        way.add(temp);
        /// add commit to Contributor
        for(Contributor s : father.Contributors)
        {
            if(s.Name.equals(Author))
            {
                s.way.add(temp);
                s.NumberOfCommit++;
            }
        }
        
        /// incres Number Of Version
        File CreateProjectDirectory = new File(projectdirector + "\\" + NumberOfVersion);
        CreateProjectDirectory.mkdir();
        UsersHowSeeLastUpdate.add(Author); 
       
        update_BENKH();
        father.NumberOfVersion++;
    }
    public void update_BENKH()
    {
        String _director = projectdirector + "\\" + father.NumberOfVersion;
        int _IdLastCommit = way.size()-1;
        String _ProjectName = father.NameProject;
        List<Contributor> _Contributors = new ArrayList<>();
        for(Contributor  s  : father.Contributors)
        {
            _Contributors.add(s);
        }
        String _BranchName = this.branchName;
        ProjectToUpload New = new ProjectToUpload(_director , _IdLastCommit , _ProjectName , _Contributors , _BranchName); 
        New.Save();
    }
    /// Create Branch have first num version from another brach
    branchClass(Project father, String branchName, branchClass fatherBranch, int num, String userCreateBranch) {
        /// need fix  /////////////////////////
        this.userCreateBranch = userCreateBranch;
        this.branchName = branchName;
        this.father = father;
        this.projectdirector = father.ProjectDirectory;
        for (int i = 0; i < num; i++) {
            CommitClass temp = fatherBranch.way.get(i);
            temp.branchName = branchName;
            way.add(temp);
        }
        
        UsersHowSeeLastUpdate.add(userCreateBranch);
        lastCommite = new Date();
        boolean ok_add = true;
        for (Contributor s : father.Contributors) {
            if (s.Name.equals(userCreateBranch)) {
                ok_add = false;
            }
        }
        if (ok_add) {
            father.Contributors.add(new Contributor(userCreateBranch) ) ;
        }

    }

    boolean UserCanaddNewVersion(String user) {
        for (String temp : UsersHowSeeLastUpdate) {
            if (temp.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /// not complet
    boolean addNewVersion(String Author, String Detail) {
        boolean Can = UserCanaddNewVersion(Author);
        if (!Can) {
            return false;
        }
        /// update Version
        UsersHowSeeLastUpdate.clear();
        UsersHowSeeLastUpdate.add(Author);
        int NumberOfVersion = father.NumberOfVersion;
        String Directory = projectdirector + "\\" + NumberOfVersion;
        File CreateProjectDirectory = new File(Directory);
        CreateProjectDirectory.mkdir();
        CommitClass temp = new CommitClass(branchName, Author, Directory, Detail, way.size() + 1);
        way.add(temp);

        lastCommite = new Date();

        boolean ok_add = true;
        for (Contributor s : father.Contributors) {
            if (s.Name.equals(Author)) {
                ok_add = false;
                s.NumberOfCommit++;
                s.way.add(temp);
            }
        }
        if (ok_add) {
            Contributor S = new Contributor(Author);
            S.NumberOfCommit++;
            S.way.add(temp);
            father.Contributors.add(S);
        }
        update_BENKH();
        father.NumberOfVersion++;
        return true;
    }

}
