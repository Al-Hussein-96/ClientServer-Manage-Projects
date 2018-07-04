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
public class GetMerge extends Command implements Serializable{
    String NameProject;
    String BranchFirst; /// current Branch (can push)
    String BranchSecond; /// another Branch

    public GetMerge(String NameProject, String BranchFirst, String BranchSecond) {
        super(CommandType.GETMERGE);
        this.NameProject = NameProject;
        this.BranchFirst = BranchFirst;
        this.BranchSecond = BranchSecond;
    }

    public String getNameProject() {
        return NameProject;
    }

    public void setNameProject(String NameProject) {
        this.NameProject = NameProject;
    }

    public String getBranchFirst() {
        return BranchFirst;
    }

    public void setBranchFirst(String BranchFirst) {
        this.BranchFirst = BranchFirst;
    }

    public String getBranchSecond() {
        return BranchSecond;
    }

    public void setBranchSecond(String BranchSecond) {
        this.BranchSecond = BranchSecond;
    }
    
    
}
