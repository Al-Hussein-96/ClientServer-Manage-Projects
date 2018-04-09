package severbenkh;

import CommonClass.CommitClass;
import CommonClass.Contributor;
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
        father.NumberOfVersion++;

    }

    /// Create Branch have first num version from another brach
    branchClass(Project father, String branchName, branchClass fatherBranch, int num, String userCreateBranch) {
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
        CommitClass temp = new CommitClass(branchName, Author, Directory, Detail, way.size() + 1);
        way.add(temp);
        father.NumberOfVersion++;
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
        return true;
    }

}
