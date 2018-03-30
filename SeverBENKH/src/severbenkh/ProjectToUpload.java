package severbenkh;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import CommonClass.ResourceManager;
public class ProjectToUpload implements Serializable{
public String director;
public int IdLastCommit;
public int Projectid; 
public List<String> Contributors = new ArrayList<>();
public String BranchName;
ProjectToUpload(String director ,int IdLastCommit , int Projectid  , List<String> Contributors ,String BranchName)
{
    this.director = director;
    this.IdLastCommit = IdLastCommit;
    this.BranchName = BranchName;
    for(String s : Contributors)
    {
        this.Contributors.add(s);
    }
   
}
public void Save()
{
     try {
        ResourceManager.save(this, director);
    } catch (Exception ex) {
        Logger.getLogger(ProjectToUpload.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
