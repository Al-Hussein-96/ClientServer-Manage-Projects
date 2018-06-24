package CommonCommand;

import java.io.Serializable;

public class GetDiffFile extends Command implements Serializable {

    public String DirFile1;
    public String DirFile2;

    public GetDiffFile(String DirFile1,String DirFile2) {
        super(CommandType.GetDiffFile);
        this.DirFile1 = DirFile1;
        this.DirFile2 = DirFile2;
    }
}
