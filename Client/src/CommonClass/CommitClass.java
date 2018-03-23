package CommonClass;

import java.io.Serializable;
import java.util.Date;

public class CommitClass implements Serializable {

    public String Directory = "";
    public String Author = "";
    public Date MyDate;
    public String Detail = "";
    public String branchName;

    public CommitClass(String branchName, String Author, String Directory, String Detail) {
        this.branchName = branchName;
        this.Author = Author;
        this.Directory = Directory;
        this.MyDate = new Date();
        this.Detail = Detail;
    }

}
