package CommonRespone;

import CommonClass.CommonProject;
import java.util.List;

public class SendAllProject extends Respone{
    List<CommonProject> mylist;
    public SendAllProject(Type TypeRespone , List<CommonProject> mylist)
    {
        super(TypeRespone);
        for(CommonProject temp : mylist)
        {
            this.mylist.add(temp);
        }
    }
}
