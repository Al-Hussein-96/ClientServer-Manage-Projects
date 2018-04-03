/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonCommand;

import java.io.Serializable;


public class StartProject extends Command implements Serializable{
    public String NameProject;
    public String Access;

    public StartProject(String NameProject, String Access) {
        super(CommandType.STARTPROJECT);
        this.NameProject = NameProject;
        this.Access = Access;
    }
    
}
