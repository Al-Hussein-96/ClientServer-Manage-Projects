package severbenkh;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.ResourceManager.load;

public class LoginClass {

    public static boolean Login(User user) {
        String directoryname="D:\\ClientServer-Manage-Projects-master\\SeverBENKH\\src\\Users Information\\"+user.getName();
        String filename=directoryname+"\\"+user.getName()+"file.data";
        try {
            User nuser=(User)load(filename);
            if(nuser.getPassword().equals(user.getPassword())){
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
//        List<User> UserList;
//        try {
//            UserList = (ArrayList) load("UserSignUp.data");
//        } catch (Exception ex) {
//            return false;
//        }
//        for (User Tempuser : UserList) {
//            if (Tempuser.getName().equals(user.getName())) {
//                return Tempuser.getPassword().equals(user.getPassword());
//            }
//        }
//        return false;
    }
}