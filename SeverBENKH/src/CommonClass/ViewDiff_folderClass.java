package CommonClass;

import static CommonClass.StateType.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewDiff_folderClass implements Serializable{
    public StateType MyState = NoChange; 
    public List <  NameAndDirectoryAndState > MyFile = new ArrayList<>();
    public List <  NameAndDirectoryAndState > MyFolder = new ArrayList<>();
    public List < ViewDiff_folderClass > MyFolderView = new ArrayList<>();;
}
