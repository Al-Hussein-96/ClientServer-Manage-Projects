package CommonCommand;

import java.io.Serializable;

public class GetProject extends Command implements Serializable{

    public String NameProject;
    
    public GetProject(String NameProject) {
        super(CommandType.GETPROJECT);
        this.NameProject = NameProject;
    }

    public GetProject(String NameProject, CommandType TypeCommand) {
        super(TypeCommand);
        this.NameProject = NameProject;
    }
    
   
}
