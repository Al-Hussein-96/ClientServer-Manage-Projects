package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonProject  implements Serializable{
    boolean Access = true;
    int id;
    String Author;
    String NameProject;
    Date DateCreate  ;
    int numberOFBranshes;
    List<String> Contributors = new ArrayList<>();
    List<String> BranchNames = new ArrayList<>();
    List < CommitClass > way = new ArrayList<>();
    
    
}
