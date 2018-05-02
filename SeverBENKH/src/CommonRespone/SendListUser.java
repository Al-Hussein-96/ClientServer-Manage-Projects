package CommonRespone;

import CommonClass.User;
import java.util.ArrayList;
import java.util.List;


public class SendListUser extends Respone{
    List<User> listUser;
    public SendListUser(List<User> listUser) {
        super(ResponeType.DONE);
        this.listUser = new ArrayList<>(listUser);
    }

    public List<User> getListUser() {
        return listUser;
    }
    
    
    
}
