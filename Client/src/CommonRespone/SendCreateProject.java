package CommonRespone;

import CommonClass.ProjectToUpload;
import java.io.Serializable;

public class SendCreateProject extends Respone implements Serializable{
    ProjectToUpload BenkhFile ; 
    public SendCreateProject(ProjectToUpload BenkhFile)
    {
        super(ResponeType.DONE);
        this.BenkhFile = BenkhFile;
    }

    public ProjectToUpload getBenkhFile() {
        return BenkhFile;
    }
    
}