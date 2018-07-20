package CommonRespone;

import java.io.Serializable;

public abstract class Respone implements Serializable{
    public ResponeType TypeRespone;
    public String Message ;



    public Respone(ResponeType TypeRespone) {
        this.TypeRespone = TypeRespone;
    }
    
    
}
