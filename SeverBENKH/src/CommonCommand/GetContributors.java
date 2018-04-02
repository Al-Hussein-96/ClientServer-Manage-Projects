package CommonCommand;

import java.io.Serializable;

public class GetContributors extends GetProject implements Serializable{
    
    public GetContributors(String NameProject) {
        super(NameProject);
    }
    
}
