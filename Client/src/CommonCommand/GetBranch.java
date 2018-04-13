package CommonCommand;

import java.io.Serializable;
public class GetBranch extends GetProject implements Serializable {

    public String BranchName;

    public GetBranch(String NameProject, String BranchName) {
        super(NameProject,CommandType.GETBRANCH);
        this.BranchName = BranchName;
    }

}
