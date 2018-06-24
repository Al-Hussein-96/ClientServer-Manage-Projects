
package Different;


public class NoChange extends Changes {

    public NoChange(String object) {
        super(object);
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visitKeepCommand(getObject());
    }
}
