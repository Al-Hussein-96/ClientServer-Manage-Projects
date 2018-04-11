package CommonCommand;

import java.io.Serializable;

public class GetListCommits extends Command implements Serializable {

    String NameProject;
    String NameBranch;

    public GetListCommits(String NameProject, String NameBranch) {
        super(CommandType.LISTCOMMITS);
        this.NameProject = NameProject;
        this.NameBranch = NameBranch;
    }

    public String getNameProject() {
        return NameProject;
    }

    public String getNameBranch() {
        return NameBranch;
    }

}
