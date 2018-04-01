package CommonRespone;

import CommonClass.ViewfolderClass;

public class SendProject extends Respone{
    public ViewfolderClass ob ; 
    public SendProject()
    {
        
    }
    public SendProject(Type TypeRespone, ViewfolderClass ob)
    {
        super(TypeRespone);
        this.ob = ob ; 
    }
}
