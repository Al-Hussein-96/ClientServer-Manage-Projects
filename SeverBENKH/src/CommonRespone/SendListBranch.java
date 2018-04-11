package CommonRespone;

import CommonClass.CommonBranch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendListBranch extends Respone implements Serializable {

    List<CommonBranch> listbranch = new ArrayList<>();

    public SendListBranch(List<CommonBranch> listbranch) {
        super(ResponeType.DONE);
        this.listbranch = listbranch;
    }

    public List<CommonBranch> getListbranch() {
        return listbranch;
    }

}
