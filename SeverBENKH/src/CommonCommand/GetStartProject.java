package CommonCommand;

import java.io.Serializable;


public class GetStartProject extends Command implements Serializable{
    public String NameProject;
    public String Access;

    public GetStartProject(String NameProject, String Access) {
        super(CommandType.STARTPROJECT);
        this.NameProject = NameProject;
        this.Access = Access;
    }
    
}
