package CommonCommand;

public class GetPull extends GetProject {

    private int idCommit;
    String BranchName;
    public GetPull(String NameProject, int idCommit, String BranchName) {
        super(NameProject, CommandType.GETPULL);
        this.idCommit = idCommit;
        this.BranchName = BranchName;
    }

    public int getIdCommit() {
        return idCommit;
    }

    public String getBranchName() {
        return BranchName;
    }

}
