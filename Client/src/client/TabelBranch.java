package client;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Al-Hussein
 */
public class TabelBranch {

    public StringProperty BranchName;
    public StringProperty UserCreateBranch;
    public StringProperty LastCommite;

    public TabelBranch(String branchName, String userCreateBranch, String lastCommite) {
        this.BranchName = new SimpleStringProperty(branchName);
        this.UserCreateBranch = new SimpleStringProperty(userCreateBranch);
        this.LastCommite = new SimpleStringProperty(lastCommite);
    }

    public String getBranchName() {
        return BranchName.get();
    }
    
    public String getUserCreateBranch() {
        return UserCreateBranch.get();
    }

    public String getLastCommite() {
        return LastCommite.get();
    }

}
