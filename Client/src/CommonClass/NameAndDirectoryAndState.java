package CommonClass;

import java.io.Serializable;

public class NameAndDirectoryAndState implements Serializable{
    public NameAndDirectory MyFile ;
    /// if State is change then we should get the two Files to compare  them
    public NameAndDirectory OldFile = null;
    public StateType MyState ; 
    public NameAndDirectoryAndState(NameAndDirectory MyFile ,StateType MyState ,NameAndDirectory OldFile )
    {
        this.MyFile = MyFile;
        this.MyState = MyState;
        this.OldFile = OldFile;
    }
}
