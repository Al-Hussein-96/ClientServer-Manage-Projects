
package Different;

public interface CommandVisitor<String> {

    void visitInsertCommand(String object);


    void visitKeepCommand(String object);


    void visitDeleteCommand(String object);
}
