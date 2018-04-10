package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonProject implements Serializable {

    public boolean Access = true;
    public int id;
    public String Author;
    public String NameProject;
    public Date DateCreate;
    public int numberOFBranshes;
    public List<Contributor> Contributors = new ArrayList<>();
    public List< CommonBranch> BranchNames = new ArrayList<>();
}
