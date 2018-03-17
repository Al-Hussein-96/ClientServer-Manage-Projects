package severbenkh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static severbenkh.ResourceManager.*;

public class SignUpClass {

    public static boolean SignUp(User user) throws FileNotFoundException, IOException {
        FileInputStream id = new FileInputStream(SeverBENKH.idFileName);
        int x = id.read();
        id.close();
        x++;
        FileOutputStream idd = new FileOutputStream(SeverBENKH.idFileName);
        idd.write(x);
        idd.close();

        String directoryname = "src\\Users Information\\" + user.getName();
        String userFileName = directoryname + "\\" + user.getName() + "information file.data";
        String projectFileName = directoryname + "\\" + user.getName() + "projects.data";

        //create a directory for each user
        File dir = new File(directoryname);
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            return false;
        }
        user.setId(x);

        //create user file contains user name and password
        try {
            save(user, userFileName);
        } catch (Exception ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create projects file that may contains projects ids and names only
        try (PrintWriter out = new PrintWriter(projectFileName)) {
            out.println("no projects yet!!!");
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
