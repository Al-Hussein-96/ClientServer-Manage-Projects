package CommonRespone;

import CommonClass.ViewfolderClass;
import java.io.Serializable;

public class SendProject extends Respone implements Serializable{

    public ViewfolderClass ob;

    public SendProject(ViewfolderClass ob) {
        super(ResponeType.DONE);
        this.ob = ob;
    }
}
