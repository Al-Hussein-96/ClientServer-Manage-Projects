package Different;

public class Insert<String> extends Changes<String> {

    public Insert(final String object) {
        super(object);
    }

    @Override
    public void accept(final CommandVisitor<String> visitor) {
        visitor.visitInsertCommand(getObject());
    }

}
