package CommonCommand;

import java.io.Serializable;

public class GetListBranch extends Command implements Serializable {
    String NameProject;

    public GetListBranch(String NameProject) {
        super(CommandType.LISTBRANCH);
        this.NameProject = NameProject;
    }

    public String getNameProject() {
        return NameProject;
    }
    
    

}
