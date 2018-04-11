/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonCommand;

import java.io.Serializable;

/**
 *
 * @author Al-Hussein
 */
public class GetListContributors extends Command implements Serializable {

    String NameProject;

    public GetListContributors(String NameProject) {
        super(CommandType.ListCONTRIBUTORS);
        this.NameProject = NameProject;
    }

    public String getNameProject() {
        return NameProject;
    }
    
    

}
