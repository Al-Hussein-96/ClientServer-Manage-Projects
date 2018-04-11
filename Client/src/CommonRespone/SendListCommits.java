package CommonRespone;

import CommonClass.CommitClass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendListCommits extends Respone implements Serializable {

    List<CommitClass> listCommit = new ArrayList<>();

    public SendListCommits(List<CommitClass> listCommit) {
        super(ResponeType.DONE);
        this.listCommit = listCommit;
    }

    public List<CommitClass> getListCommit() {
        return listCommit;
    }
}
