package CommonRespone;

import CommonClass.CommonProject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendMyProject extends Respone implements Serializable{

    List<CommonProject> mylist = new ArrayList<>();

    public SendMyProject(List<CommonProject> mylist) {
        super(ResponeType.DONE);
        for (CommonProject temp : mylist) {
            this.mylist.add(temp);
        }
    }

    public List<CommonProject> getMylist() {
        return mylist;
    }
    
    
}
