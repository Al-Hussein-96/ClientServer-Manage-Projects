package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Serializable{
    User user;

    List<CommonProject> OwnProject = new ArrayList<>();
    List<CommonProject> ContributorProject = new ArrayList<>();

    public Profile(User user, List<CommonProject> OwnProject, List<CommonProject> ContributorProject) {
        this.user = user;

        for (CommonProject u : OwnProject) {
            this.OwnProject.add(u);
        }

        for (CommonProject u : ContributorProject) {
            this.OwnProject.add(u);
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

}
