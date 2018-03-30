package CommonClass;

import java.io.Serializable;
import java.util.Date;

public class CommitClass implements Serializable {

    public String Directory;
    public String Author;
    public Date MyDate;
    public String Detail;
    public String branchName;
    public int Id;

    public CommitClass(String branchName, String Author, String Directory, String Detail, int Id) {

        this.Directory = Directory;
        this.Author = Author;
        this.Detail = Detail;
        this.branchName = branchName;
        this.MyDate = new Date();
        this.Id = Id;
    }

}
