package severbenkh;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Project implements Serializable {

    int id;
    String userCreateProject;
    String ProjectName;
    String ProjectDirectory;
    /// Names of users work in project
    List<String> users = new ArrayList<>();
    int numberOFBranshes;
    int NumberOfVersion;
    List< branchClass> branchListClass = new ArrayList<>();

    // add project to projects file
    /// don't make new project if ProjectDirectory is used from another project
    Project(String userCreateProject, String ProjectName, String ProjectDirectory) {
        this.userCreateProject = userCreateProject;
        this.ProjectName = ProjectName;
        this.ProjectDirectory = ProjectDirectory;
        users.add(userCreateProject);
        NumberOfVersion = 1;
        numberOFBranshes = 1;
        branchClass branchMaster = new branchClass(this, "Master");
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCreateProject() {
        return userCreateProject;
    }

    public void setUserCreateProject(String userCreateProject) {
        this.userCreateProject = userCreateProject;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }

    public String getProjectDirectory() {
        return ProjectDirectory;
    }

    public void setProjectDirectory(String ProjectDirectory) {
        this.ProjectDirectory = ProjectDirectory;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public int getNumberOFBranshes() {
        return numberOFBranshes;
    }

    public void setNumberOFBranshes(int numberOFBranshes) {
        this.numberOFBranshes = numberOFBranshes;
    }

    public int getNumberOfVersion() {
        return NumberOfVersion;
    }

    public void setNumberOfVersion(int NumberOfVersion) {
        this.NumberOfVersion = NumberOfVersion;
    }

    public List<branchClass> getBranchListClass() {
        return branchListClass;
    }

    public void setBranchListClass(List<branchClass> branchListClass) {
        this.branchListClass = branchListClass;
    }
    
    
    

}
