
package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewfolderClass implements Serializable{
    public  List <  NameAndDirectory > MyFile = new ArrayList<>();
    public List <  NameAndDirectory > MyFolder = new ArrayList<>();
    public List < ViewfolderClass > MyFolderView = new ArrayList<>();;

   
}