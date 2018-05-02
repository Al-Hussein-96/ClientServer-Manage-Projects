package severbenkh;

import CommonClass.ResourceManager;
import CommonClass.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static CommonClass.ResourceManager.save;
import java.io.Serializable;
import java.util.List;
import static severbenkh.SeverBENKH.list_user_in_server;

public class SignUpClass {

    public static boolean SignUp(User user) throws FileNotFoundException, IOException {
        FileInputStream id = new FileInputStream(SeverBENKH.idFileName);
        int x = id.read();
        id.close();
        x++;
        FileOutputStream idd = new FileOutputStream(SeverBENKH.idFileName);
        idd.write(x);
        idd.close();

        String directoryname = SeverBENKH.usersdirectoryName + "\\" + user.getName();
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
        try {
            List < User > User_in_server = (List < User >) ResourceManager.load(list_user_in_server);
            User_in_server.add(user);
            ResourceManager.save((Serializable) User_in_server, list_user_in_server);
        } catch (Exception ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create projects file that may contains projects ids and names only
        try (PrintWriter out = new PrintWriter(projectFileName)) {
            out.println("no projects yet!!!");
        }
        return true;
    }

}
