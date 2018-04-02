package CommonRespone;

import java.io.Serializable;

public class SendCreateProject extends Respone implements Serializable{
    public int IdProject ; 
    public String Author ; 
    public SendCreateProject(int IdProject , String Author)
    {
        super(ResponeType.DONE);
        this.IdProject = IdProject;
        this.Author = Author;
    }
}
