package Different;

import java.io.Serializable;

public class Delete extends Changes {

    public Delete(String object) {
        super(object);
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visitDeleteCommand(getObject());
    }
}
