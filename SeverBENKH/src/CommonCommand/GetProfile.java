package CommonCommand;

import CommonClass.User;

public class GetProfile extends Command {
    String UserName;
    
    public GetProfile(String UserName) {
        super(CommandType.GETPROFILE);
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }



}
