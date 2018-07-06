package CommonRespone;

import CommonClass.ViewDiff_folderClass;
import java.io.Serializable;

public class SendProject_Merge extends Respone implements Serializable{
    public ViewDiff_folderClass ob;
     public SendProject_Merge(ViewDiff_folderClass ob)
    {
        super(ResponeType.DONE);
        this.ob = ob ; 
    }
}
