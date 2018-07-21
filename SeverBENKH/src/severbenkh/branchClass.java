package severbenkh;

import CommonClass.CommitClass;
import CommonClass.Contributor;
import CommonClass.ProjectToUpload;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class branchClass implements Serializable {

    List< CommitClass> way = new ArrayList<>();
    String projectdirector;
    /// list of user download last Version
    List<ProjectToUpload> BENKH_File = new ArrayList<>();
    Date lastCommite;
    Project father;
    String branchName;
    String userCreateBranch;
    /// for merge
    public int BranchLevel = 0;
    String BranchFather = null;
    int CommitFather = 0;
    branchClass(Project father, String branchName, String Author) {
        this.userCreateBranch = Author;
        this.branchName = branchName;
        /// for merge
        this.BranchFather = branchName;
        
        lastCommite = new Date();
        this.father = father;
        this.projectdirector = father.ProjectDirectory;
        int NumberOfVersion = father.NumberOfVersion;
 
        String Directory = this.projectdirector + "\\" + NumberOfVersion;
        // for merge
        this.CommitFather = 1;
        
  
        CommitClass temp = new CommitClass(branchName, Author, Directory, "", 1);
        way.add(temp);
        /// add commit to Contributor
        for (Contributor s : father.Contributors) {
            if (s.Name.equals(Author)) {
                s.way.add(temp);
                s.NumberOfCommit++;
            }
        }

        /// incres Number Of Version
        File CreateProjectDirectory = new File(projectdirector + "\\" + NumberOfVersion);
        CreateProjectDirectory.mkdir();

        update_BENKH();
        father.NumberOfVersion++;
    }

    public void update_BENKH() {
        String _director = projectdirector + "\\" + father.NumberOfVersion;
        int _IdLastCommit = way.size() - 1;
        String _ProjectName = father.NameProject;
        List<Contributor> _Contributors = new ArrayList<>();
        for (Contributor s : father.Contributors) {
            _Contributors.add(s);
        }
        String _BranchName = this.branchName;
        ProjectToUpload New = new ProjectToUpload(_director, _IdLastCommit, _ProjectName, _Contributors, _BranchName);
        BENKH_File.add(New);
    }

    /// Create Branch have first num version from another brach
    branchClass(Project father, String branchName, branchClass fatherBranch, int num, String userCreateBranch) {
        this.userCreateBranch = userCreateBranch;
        this.branchName = branchName;
        /// for merge
        this.BranchLevel = fatherBranch.BranchLevel+1;
        this.BranchFather = fatherBranch.branchName;
        
        this.father = father;
        this.projectdirector = father.ProjectDirectory;
        for (int i = 0; i < num; i++) {
            CommitClass temp = fatherBranch.way.get(i).clone();
            temp.branchName = branchName;
            this.way.add(temp);
            ProjectToUpload temp2 = null;
            temp2 = fatherBranch.BENKH_File.get(i).clone();
            temp2.BranchName = branchName;
            this.BENKH_File.add(temp2);
            /// for merge /// final one is the father
            if(i == num-1)
              this.CommitFather = temp.Id;
        }

        lastCommite = new Date();
        boolean ok_add = true;
        for (Contributor s : father.Contributors) {
            if (s.Name.equals(userCreateBranch)) {
                ok_add = false;
            }
        }
        if (ok_add) {
            father.Contributors.add(new Contributor(userCreateBranch));
        }
        /// here we should copy file from last directory 
        ///     copy_File_for_branch();

        father.Save();

    }

    boolean UserCanaddNewVersion(String user) {
        for (Contributor s : father.Contributors) {
            if (s.Name.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /// not complet
    /**
     *
     * @param Author
     * @param Detail Comment Of User for this Commit
     * @return
     */
    CommitClass addNewVersion(String Author, String Detail) {
        boolean Can = UserCanaddNewVersion(Author);
        if (!Can) {
            return null;
        }
        /// update Version
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
        return temp;
    }

}
