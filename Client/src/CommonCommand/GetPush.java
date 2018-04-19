package CommonCommand;

import CommonClass.ProjectToUpload;
import java.io.Serializable;

public class GetPush extends GetProject implements Serializable {

    ProjectToUpload hiddenFile;
    String NameFolderSelect;
    public String CommentUser;

    public GetPush(String NameProject, ProjectToUpload hiddenFile, String NameFolderSelect,String CommentUser) {
        super(NameProject, CommandType.GETPUSH);
        this.hiddenFile = hiddenFile;
        this.NameFolderSelect = NameFolderSelect;
        this.CommentUser = CommentUser;
    }

    public ProjectToUpload getHiddenFile() {
        return hiddenFile;
    }

    public String getNameFolderSelect() {
        return NameFolderSelect;
    }

}
