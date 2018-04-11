package CommonCommand;

import CommonClass.User;
import java.io.Serializable;

public class GetLOGIN extends Command implements Serializable{
    public User user;

    public GetLOGIN(User user) {
        super(CommandType.LOGIN);
        this.user = user;
    }
}
