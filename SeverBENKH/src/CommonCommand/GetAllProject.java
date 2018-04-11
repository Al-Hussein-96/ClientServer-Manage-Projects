
package CommonCommand;

import java.io.Serializable;

public class GetAllProject extends Command implements Serializable{

    public GetAllProject() {
        super(CommandType.ALLPROJECT);
    }

}
