
package Different;



public class Delete<String> extends Changes<String> {

    public Delete(final String object) {
        super(object);
    }

    @Override
    public void accept(final CommandVisitor<String> visitor) {
        visitor.visitDeleteCommand(getObject());
    }
}
