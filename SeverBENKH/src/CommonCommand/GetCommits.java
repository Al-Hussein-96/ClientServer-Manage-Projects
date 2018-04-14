package CommonCommand;

import java.io.Serializable;

public class GetCommits extends GetProject implements Serializable {

    public String BranchName;
    public int IDCommit;

    public GetCommits(String NameProject, String BranchName, int IDCommit) {
        super(NameProject, CommandType.GETCOMMITS);
        this.BranchName = BranchName;
        this.IDCommit = IDCommit;
    }
}
