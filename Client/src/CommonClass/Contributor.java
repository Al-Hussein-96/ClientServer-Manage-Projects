package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contributor implements Serializable{
    public String Name ; 
    public int NumberOfCommit ; 
    public List< CommitClass> way = new ArrayList<>();
    public Contributor(String Name)
    {
        this.Name = Name;
        NumberOfCommit = 0;
    }
}
