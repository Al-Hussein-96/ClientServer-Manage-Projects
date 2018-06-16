package Different;

public abstract class Changes<String> {

    private final String object;

    protected Changes(final String object) {
        this.object = object;
    }

    protected String getObject() {
        return object;
    }

    public abstract void accept(CommandVisitor<String> visitor);

}
