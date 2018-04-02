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
public class AllProject extends Command implements Serializable{
    
    public AllProject() {
        super(CommandType.ALLPROJECT);
    }
    
}
