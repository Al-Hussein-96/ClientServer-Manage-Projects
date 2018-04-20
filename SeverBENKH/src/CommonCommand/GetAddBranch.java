package CommonCommand;

import java.io.Serializable;

public class GetAddBranch extends GetProject implements Serializable{

    String BranchName;

    public GetAddBranch(String NameProject, String BranchName) {
        super(NameProject);
        this.BranchName = BranchName;
    }

    public String getBranchName() {
        return BranchName;
    }

}
