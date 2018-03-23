package CommonClass;

import java.io.Serializable;
import java.util.Date;

public class CommitClass implements Serializable {



    public String Directory;
    public String Author;
    public Date MyDate;
    public String Detail;
    public String branchName;
    
    
    
    
    public CommitClass(String Directory, String Author, String Detail, String branchName) {
        this.Directory = Directory;
        this.Author = Author;
        this.Detail = Detail;
        this.branchName = branchName;
        this.MyDate = new Date();
    }

}
