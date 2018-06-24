
package Different;

public interface CommandVisitor {

    void visitInsertCommand(String object);


    void visitKeepCommand(String object);


    void visitDeleteCommand(String object);
}
