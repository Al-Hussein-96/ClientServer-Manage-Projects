package CommonCommand;

import CommonClass.User;
import java.io.Serializable;

public class LOGIN extends Command implements Serializable{
    public User user;

    public LOGIN(User user) {
        super(CommandType.LOGIN);
        this.user = user;
    }
}
