package severbenkh;

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
        //create projects file that may contains projects ids and names only
        try (PrintWriter out = new PrintWriter(projectFileName)) {
            out.println("no projects yet!!!");
        }
        return true;
    }

}
