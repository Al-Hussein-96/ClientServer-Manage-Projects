
package Different;


public class NoChange<String> extends Changes<String> {

    public NoChange(final String object) {
        super(object);
    }

    @Override
    public void accept(final CommandVisitor<String> visitor) {
        visitor.visitKeepCommand(getObject());
    }
}
