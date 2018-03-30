package severbenkh;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static CommonClass.ResourceManager.load;

public class LoginClass {

    public static boolean Login(User user) {
        String directoryname= SeverBENKH.usersdirectoryName +"\\"+user.getName();
        String filename=directoryname+"\\"+user.getName()+"information file.data";
        try {
            User nuser=(User)load(filename);
            if(nuser.getPassword().equals(user.getPassword())){
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
