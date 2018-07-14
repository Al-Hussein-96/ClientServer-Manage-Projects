package CommonRespone;

import CommonClass.CommonProject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendAllProject extends Respone implements Serializable {

    List<CommonProject> mylist = new ArrayList<>();

    public SendAllProject(ResponeType TypeRespone, List<CommonProject> mylist) {
        super(TypeRespone);
        for (CommonProject temp : mylist) {
            this.mylist.add(temp);
        }
    }

    public List<CommonProject> getMylist() {
        return mylist;
    }

}
