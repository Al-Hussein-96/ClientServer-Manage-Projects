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
public class GetBranch extends GetProject implements Serializable{
    
    public GetBranch(String NameProject) {
        super(NameProject);
    }
    
    
}