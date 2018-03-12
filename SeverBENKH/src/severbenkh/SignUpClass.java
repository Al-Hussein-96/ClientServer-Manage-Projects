package severbenkh;

import java.io.File;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.ResourceManager.*;

public class SignUpClass {
    
    public static boolean SignUp(User user)
    {
        int id = 1;
        List <User> UserList = new ArrayList<>();
        try {
            UserList = (ArrayList)load("UserSignUp.data");
        } catch (Exception ex) {
           user.Id = id;
           UserList.add(user);
          return true;
        }
        for(User Tempuser : UserList)
        {
            if(Tempuser.Name.equals(user.Name))
            {
                return false;
            }
            id++;   
        }
        user.Id = id;
        UserList.add(user);
        return true;
    }
    
}
