package CommonCommand;

import CommonClass.User;
import java.io.Serializable;

public class GetSIGNUP extends Command implements Serializable{

    public User user;

    public GetSIGNUP(User user) {
        super(CommandType.SIGNUP);
        this.user = user;
    }
}
