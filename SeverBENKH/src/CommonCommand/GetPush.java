package CommonCommand;

import CommonClass.ProjectToUpload;
import java.io.Serializable;

public class GetPush extends GetProject implements Serializable {

    ProjectToUpload hiddenFile;
    String NameFolderSelect;
    public GetPush(String NameProject, ProjectToUpload hiddenFile,String NameFolderSelect) {
        super(NameProject, CommandType.GETPUSH);
        this.hiddenFile = hiddenFile;
        this.NameFolderSelect = NameFolderSelect;
    }

    public ProjectToUpload getHiddenFile() {
        return hiddenFile;
    }

    public String getNameFolderSelect() {
        return NameFolderSelect;
    }
    
    
}
