package CommonClass;

import java.io.Serializable;

public class NameAndDirectory implements Serializable {

    public String Name;
    public long Size;
    public long DateModified;
    public String Directory;
    public String Description;

    public NameAndDirectory(String Name, long Size, long DateModified, String Directory) {
        this.Name = Name;
        this.Size = Size;
        this.DateModified = DateModified;
        this.Directory = Directory;
        this.Description = "";
    }
}
