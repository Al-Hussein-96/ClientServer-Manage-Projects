/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonRespone;

import CommonClass.ViewfolderClass;
import java.io.Serializable;

/**
 *
 * @author Al-Hussein
 */
public class SendCommits extends SendProject implements Serializable{
    
    public SendCommits(ViewfolderClass ob) {
        super(ob);
    }
    
}
