package CommonCommand;
 
import java.io.Serializable;
 
 
public class GetStartProject extends Command implements Serializable{
    public String NameProject;
    public String Access;
    String NameFolderSelect;
 
    public GetStartProject(String NameProject, String Access, String NameFolderSelect) {
        super(CommandType.STARTPROJECT);
        this.NameProject = NameProject;
        this.Access = Access;
        this.NameFolderSelect = NameFolderSelect;
    }
    public String getNameFolderSelect() {
        return NameFolderSelect;
    }
 
}