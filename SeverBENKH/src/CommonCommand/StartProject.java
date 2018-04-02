package CommonCommand;

import java.io.Serializable;


public class StartProject extends Command implements Serializable{
    public String NameProject;
    public String Access;

    public StartProject(CommandType TypeCommand) {
        super(TypeCommand);
    }
    
}
