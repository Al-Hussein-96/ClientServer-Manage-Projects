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
public class GetDataBranch extends GetProject implements Serializable {

    public String BranchName;

    public GetDataBranch(String NameProject, String BranchName) {
        super(NameProject,CommandType.GETBRANCH);
        this.BranchName = BranchName;
    }

}
