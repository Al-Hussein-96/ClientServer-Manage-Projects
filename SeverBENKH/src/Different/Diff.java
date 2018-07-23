package Different;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Diff implements Serializable {

    private final List<Changes> commands;
    private int lcsLength;
    private int modifications;

    public Diff() {
        commands = new ArrayList<>();
        lcsLength = 0;
        modifications = 0;
    }

    public void append(NoChange command) {
        commands.add(command);
        ++lcsLength;
    }

    public void append(Insert command) {
        commands.add(command);
        ++modifications;
    }

    public void append(Delete command) {
        commands.add(command);
        ++modifications;
    }

    public int getLCSLength() {
        return lcsLength;
    }

    public int getModifications() {
        return modifications;
    }
    
    public List<Changes> getChanges(){
        return commands;
    }

}
