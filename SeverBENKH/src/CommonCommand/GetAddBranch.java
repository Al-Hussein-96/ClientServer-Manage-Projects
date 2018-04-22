package CommonCommand;

import java.io.Serializable;

public class GetAddBranch extends GetProject implements Serializable{

    
    public String BranchName;
    public String BranchFather;
    public int idCommit;
    public GetAddBranch(String NameProject, String BranchName) {
        super(NameProject);
        this.BranchName = BranchName;
    }

    public String getBranchName() {
        return BranchName;
    }

}
