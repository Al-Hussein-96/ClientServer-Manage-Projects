
package CommonCommand;

import java.io.Serializable;


public abstract class Command implements Serializable{
    public CommandType TypeCommand; 

    public Command(CommandType TypeCommand) {
        this.TypeCommand = TypeCommand;
    }

    public CommandType getTypeCommand() {
        return TypeCommand;
    }
    
    
    
}
