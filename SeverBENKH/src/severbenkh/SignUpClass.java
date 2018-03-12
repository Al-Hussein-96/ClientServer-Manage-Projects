package severbenkh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static severbenkh.ResourceManager.*;

public class SignUpClass {

    public static boolean SignUp(User user) {
        int id = 1;
        List<User> UserList = new ArrayList<>();
        try {
            UserList = (ArrayList) load("UserSignUp.data");
        } catch (Exception ex) {
            user.setId(id);
            UserList.add(user);
            SaveUserList(UserList);
            return true;
        }
        for (User Tempuser : UserList) {
            if (Tempuser.getName().equals(user.getName())) {
                return false;
            }
            id++;
        }
        user.setId(id);
        UserList.add(user);
        SaveUserList(UserList);
        return true;
    }

    private static void SaveUserList(List<User> UserList) {
        try {
            save((Serializable) UserList, "UserSignUp.data");
        } catch (Exception e) {
            System.out.println("Cann't Save");
        }
    }

}
