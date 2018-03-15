/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package severbenkh;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Najib
 */
public class Project {
    int id;
    String userName;
    String projectName;
    List<User>users;
    Date lastCommite;
    int numberOFBranshes;
    
    // add project to projects file
    public static void addProject(Project project) throws IOException{
        String fileName=SeverBENKH.ProjectFileName;
        try {
            ResourceManager.save((Serializable) project,fileName);
        } catch (Exception ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
