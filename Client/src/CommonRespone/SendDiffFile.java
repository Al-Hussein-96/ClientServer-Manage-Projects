
package CommonRespone;

import java.io.Serializable;

public class SendDiffFile extends Respone implements Serializable{
    //// Here Must be List of size (Number Of Line) , every Line must Have Diff
    public SendDiffFile() {
        super(ResponeType.DONE);
    }
    
}
