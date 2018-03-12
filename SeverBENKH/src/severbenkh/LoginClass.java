package severbenkh;

import java.util.ArrayList;
import java.util.List;
import static severbenkh.ResourceManager.load;

public class LoginClass {

    public static boolean Login(User user) {
        List<User> UserList;
        try {
            UserList = (ArrayList) load("UserSignUp.data");
        } catch (Exception ex) {
            return false;
        }
        for (User Tempuser : UserList) {
            if (Tempuser.getName().equals(user.getName())) {
                return Tempuser.getPassword().equals(user.getPassword());
            }
        }
        return false;
    }
}
