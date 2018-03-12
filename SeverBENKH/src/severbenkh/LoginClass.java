package severbenkh;

import java.util.ArrayList;
import java.util.List;
import static severbenkh.ResourceManager.load;

public class LoginClass {
    public static boolean Login(User user)
    {
        List <User> UserList = new ArrayList<>();
        try {
            UserList = (ArrayList)load("UserSignUp.data");
        } catch (Exception ex) {
           return false;
        }
        for(User Tempuser : UserList)
        {
            if(Tempuser.Name.equals(user.Name))
            {
                if(Tempuser.Password.equals(user.Password))
                    return true;
                return false;
            }
        }
       return false;
    }
}
