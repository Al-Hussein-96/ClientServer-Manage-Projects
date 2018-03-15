package severbenkh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.ResourceManager.*;

public class SignUpClass {

    public static boolean SignUp(User user) {
        int id = 1;
        String directoryname="D:\\ClientServer-Manage-Projects-master\\SeverBENKH\\src\\Users Information\\"+user.getName();
        String filename=directoryname+"\\"+user.getName()+"file.data";
        
        File dir=new File(directoryname);
        if(!dir.exists()){
            dir.mkdir();
        }else {
            return false;
        }
        user.setId(id);
        try {
            save(user, filename);
        } catch (Exception ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
//        List<User> UserList = new ArrayList<>();
//        try {
//            UserList = (ArrayList) load("UserSignUp.data");
//        } catch (Exception ex) {
//            user.setId(id);
//            UserList.add(user);
//            SaveUserList(UserList);
//            return true;
//        }
//        for (User Tempuser : UserList) {
//            if (Tempuser.getName().equals(user.getName())) {
//                return false;
//            }
//            id++;
//        }
//        user.setId(id);
//        UserList.add(user);
//        SaveUserList(UserList);
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
