package client;

import CommonClass.CommonBranch;
import java.text.SimpleDateFormat;
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

    public boolean equal(CommonBranch CB) {
//        System.out.println(this.NameProject.get() + " : " + CP.NameProject);
//        System.out.println(this.DateCreate.get() + " : " + CP.DateCreate);
//        System.out.println(this.Author.get() + " : " + CP.Author);
//        System.out.println(this.NumberOfContributors.get() + " : " + CP.Contributors.size());
//        int numCommit=0;
//            for(CommonBranch t : CP.BranchNames){
//                numCommit+=t.way.size();
//            }
//        System.out.println(this.NumberOfCommits.get() + " : " + numCommit);

        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");

        if (this.BranchName.get().equals(CB.branchName)
                && this.LastCommite.get().equals(ft.format(CB.lastCommite)) 
                && this.UserCreateBranch.get().equals(CB.userCreateBranch)) {
            return true;
        }
        return false;
    }
}
