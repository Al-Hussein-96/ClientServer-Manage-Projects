package CommonCommand;

import java.io.Serializable;

public class GetAddBranch extends GetProject implements Serializable {

    public String BranchName;
    public String BranchFather;
    public int idCommit;

    public GetAddBranch(String NameProject, String BranchName, String BranchFather, int idCommit) {
        super(NameProject);
        this.BranchName = BranchName;
        this.BranchFather = BranchFather;
        this.idCommit = idCommit;
    }

    public String getBranchName() {
        return BranchName;
    }

    public String getBranchFather() {
        return BranchFather;
    }

    public int getIdCommit() {
        return idCommit;
    }

}
