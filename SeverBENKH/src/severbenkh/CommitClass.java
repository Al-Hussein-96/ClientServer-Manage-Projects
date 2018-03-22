package severbenkh;

import java.io.Serializable;
import java.util.Date;

public class CommitClass implements  Serializable{
    String Directory = "";
    String Author = ""; 
    Date MyDate ;
    String Detail = "";
    String branchName ; 
    CommitClass(String branchName , String Author , String Directory , String Detail)
    {
        this.branchName = branchName;
        this.Author = this.Author;
        this.Directory = Directory;
        MyDate = new Date();
        this.Detail = Detail;
    }
    
}
