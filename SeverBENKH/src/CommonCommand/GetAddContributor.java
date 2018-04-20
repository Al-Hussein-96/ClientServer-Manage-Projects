package CommonCommand;

import java.io.Serializable;

public class GetAddContributor extends GetProject implements Serializable {

    String UserName;

    public GetAddContributor(String NameProject, String UserName) {
        super(NameProject);
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }

}
