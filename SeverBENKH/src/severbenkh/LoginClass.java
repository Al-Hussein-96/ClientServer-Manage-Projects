package severbenkh;

import CommonClass.User;
import static severbenkh.ResourceManager.load;

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
