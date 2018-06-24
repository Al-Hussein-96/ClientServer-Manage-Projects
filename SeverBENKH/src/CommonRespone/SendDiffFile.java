package CommonRespone;

import Different.Diff;
import java.io.Serializable;

public class SendDiffFile extends Respone implements Serializable {

    //// Here Must be List of size (Number Of Line) , every Line must Have Diff
    public Diff Difference = new Diff();

    public SendDiffFile(Diff Difference) {
        super(ResponeType.DONE);
        this.Difference = Difference;
    }

}
