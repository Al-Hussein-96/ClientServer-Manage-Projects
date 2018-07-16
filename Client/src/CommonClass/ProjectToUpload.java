package CommonClass;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectToUpload implements Serializable {

    public String director;
    public int IdLastCommit;
    public String ProjectName;
    public List<Contributor> Contributors = new ArrayList<>();
    public String BranchName;
    
    /// for right Click
    public String LocalUserName ; 
    public String LocalUserPassword;
    
    public void add_Local_User(User temp)
    {
        this.LocalUserName = temp.getName();
        this.LocalUserPassword = temp.getPassword();
    }
    public ProjectToUpload(String director, int IdLastCommit, String ProjectName, List<Contributor> Contributors, String BranchName) {
        this.ProjectName = ProjectName;
        this.director = director + "\\BEHKN.BEHKN";
        this.IdLastCommit = IdLastCommit;
        this.BranchName = BranchName;
        for (Contributor s : Contributors) {
            this.Contributors.add(s);
        }

    }

    @Override
    public ProjectToUpload clone() {
        String director=new  String(this.director);
        int IdLastCommit = this.IdLastCommit;
        String ProjectName=new String(this.ProjectName);
        String BranchName =new String(this.BranchName);
        List<Contributor> C=new ArrayList<>(Contributors);
        
        ProjectToUpload PTU = new ProjectToUpload(director, IdLastCommit, ProjectName, C, BranchName);
        return PTU;
    }
    
    

    private void Hide(String director) {
        try {
            File hide = new File(director);

            Path path = Paths.get(director);

            Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            Logger.getLogger(ProjectToUpload.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
