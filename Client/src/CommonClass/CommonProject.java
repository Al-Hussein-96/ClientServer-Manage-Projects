package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonProject implements Serializable {

    public boolean Access = true;
    public int id;
    public String NameProject;
    public Date DateCreate;
    public String Author;
    public int numberOFBranshes;
    public List<String> Contributors = new ArrayList<>();
    public List<String> BranchNames = new ArrayList<>();
    public List< CommitClass> way = new ArrayList<>();

}
