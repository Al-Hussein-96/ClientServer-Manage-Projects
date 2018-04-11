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
public class GetCommits extends GetProject implements Serializable {

    public String BranchName;
    public int IDCommit;

    public GetCommits(String NameProject, String BranchName, int IDCommit) {
        super(NameProject, CommandType.GETCOMMITS);
        this.BranchName = BranchName;
        this.IDCommit = IDCommit;
    }

}
