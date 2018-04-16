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

    public ProjectToUpload(String director, int IdLastCommit, String ProjectName, List<Contributor> Contributors, String BranchName) {
        this.ProjectName = ProjectName;
        this.director = director + "\\BEHKN.BEHKN";
        this.IdLastCommit = IdLastCommit;
        this.BranchName = BranchName;
        System.out.println(director+" Branch");
        for (Contributor s : Contributors) {
            this.Contributors.add(s);
        }

    }

    public void Save() {
        try {
            ResourceManager.save(this, director);
            Hide(director);
        } catch (Exception ex) {
            Logger.getLogger(ProjectToUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
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
