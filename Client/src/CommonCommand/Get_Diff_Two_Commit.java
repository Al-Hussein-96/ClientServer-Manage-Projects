package CommonCommand;

import java.io.Serializable;

public class Get_Diff_Two_Commit extends Command implements Serializable{
    public String NameProject;
    public String BranchName;
    public int IDCommitOne , IDCommitTwo;
    public Get_Diff_Two_Commit(CommandType TypeCommand,String NameProject,String BranchName,int IDCommitOne,int IDCommitTow) {
        super(TypeCommand);
        this.NameProject = NameProject;
        this.BranchName = BranchName;
        this.IDCommitOne = IDCommitOne;
        this.IDCommitTwo = IDCommitTow;
    }
    
    
}
