package Different;

import java.util.ArrayList;
import java.util.List;


public class Diff<String> {
    private final List<Changes<String>> commands;
    private int lcsLength;

    private int modifications;
 
    public Diff() {
        commands = new ArrayList<>();
        lcsLength = 0;
        modifications = 0;
    }

    public void append(final NoChange<String> command) {
        commands.add(command);
        ++lcsLength;
    }

    public void append(final Insert<String> command) {
        commands.add(command);
        ++modifications;
    }

    public void append(final Delete<String> command) {
        commands.add(command);
        ++modifications;
    }


    public void visit(final CommandVisitor<String> visitor) {
        for (final Changes<String> command : commands) {
            command.accept(visitor);
        }
    }

    public int getLCSLength() {
        return lcsLength;
    }

    public int getModifications() {
        return modifications;
    }

}
