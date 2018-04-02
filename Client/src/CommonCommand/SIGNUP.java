package CommonCommand;

import CommonClass.User;
import java.io.Serializable;

public class SIGNUP extends Command implements Serializable{

    public User user;

    public SIGNUP(User user) {
        super(CommandType.SIGNUP);
        this.user = user;
    }
}
