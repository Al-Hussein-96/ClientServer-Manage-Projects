package Different;

public class Insert extends Changes {

    public Insert(String object) {
        super(object);
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visitInsertCommand(getObject());
    }

}
