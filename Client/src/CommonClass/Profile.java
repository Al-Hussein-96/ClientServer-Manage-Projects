package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Serializable {

    User user;

    List<CommonProject> OwnProject = new ArrayList<>();
    List<CommonProject> ContributorProject = new ArrayList<>();
    List<CommonProject> ProjectsFollow_up = new ArrayList<>();

    public Profile(User user,List<CommonProject> OwnProject,List<CommonProject> ContributorProject,List<CommonProject> ProjectsFollow_up) {
        this.user = user;
        for (CommonProject u : OwnProject) {
            this.OwnProject.add(u);
        }

        for (CommonProject u : ContributorProject) {
            this.ContributorProject.add(u);
        }
        
        for (CommonProject u : ProjectsFollow_up) {
            this.ProjectsFollow_up.add(u);
        }
    }

    public User getUser() {
        return user;
    }

    public List<CommonProject> getOwnProject() {
        return OwnProject;
    }

    public List<CommonProject> getContributorProject() {
        return ContributorProject;
    }
    
    public List<CommonProject> getProjectsFollow_up() {
        return ProjectsFollow_up;
    }

}
