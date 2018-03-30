
package CommonClass;

import java.io.Serializable;

public class NameAndDirectory implements Serializable{
    public String Name;
    public String Directory;
    public NameAndDirectory(String Name , String Directory)
    {
        this.Name = Name;
        this.Directory = Directory;
    }
}
