package CommonRespone;

import java.io.Serializable;

public class SendCreateProject extends Respone implements Serializable{
    public String ProjectName ; 
    public String Author ; 
    public SendCreateProject(String ProjectName , String Author)
    {
        super(ResponeType.DONE);
        this.ProjectName = ProjectName;
        this.Author = Author;
    }
}