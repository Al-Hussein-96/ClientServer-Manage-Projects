package CommonRespone;

import CommonClass.CommonProject;
import java.util.List;


public class SendMyProject extends Respone{
    List<CommonProject> mylist;
    public SendMyProject(Type TypeRespone , List<CommonProject> mylist)
    {
        super(TypeRespone);
        for(CommonProject temp : mylist)
        {
            this.mylist.add(temp);
        }
    }
}
