package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonBranch implements Serializable{

    public List< CommitClass> way = new ArrayList<>();
    public Date lastCommite;
    public String branchName;
    public String userCreateBranch;

    public CommonBranch(Date lastCommite, String branchName, String userCreateBranch, List< CommitClass> way) {
        this.lastCommite = lastCommite;
        this.branchName = branchName;
        this.userCreateBranch = userCreateBranch;
        for (CommitClass temp : way) {
            this.way.add(temp);
        }
    }
}
