package CommonRespone;

import CommonClass.ViewDiff_folderClass;

public class Send_Diff_Two_Commit extends Respone {
    public ViewDiff_folderClass ob;
    public Send_Diff_Two_Commit(ViewDiff_folderClass ob)
    {
        super(ResponeType.DONE);
        this.ob = ob;
    
    }

}
