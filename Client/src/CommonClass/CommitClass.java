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
    @Override
    public CommitClass clone() {
        String Directory = new String(this.Directory);
        String Author = new String(this.Author);
        Date MyDate = this.MyDate;
        String Detail = new String(this.Detail);
        String branchName = new String(this.branchName);
        int id = this.Id;
        CommitClass PTU = new CommitClass(branchName, Author, Directory, Detail, Id);
        PTU.MyDate = this.MyDate;
        return PTU;
    }
}
