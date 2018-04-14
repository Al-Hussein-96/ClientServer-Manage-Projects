package client;

import CommonClass.CommitClass;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Moaz
 */
public class TabelCommit {
    
    public StringProperty Author;
    public StringProperty BranchName;
    public StringProperty Id;
    public StringProperty MyDate;
    public StringProperty Detail;
    
    public TabelCommit(String Author, String BranchName, String Id, String MyDate, String Detail) {
        this.Author = new SimpleStringProperty(Author);
        this.BranchName = new SimpleStringProperty(BranchName);
        this.Id = new SimpleStringProperty(Id);
        this.MyDate = new SimpleStringProperty(MyDate);
        this.Detail = new SimpleStringProperty(Detail);
    }

    public String getAuthor() {
        return Author.get();
    }
    
    public String getBranchName() {
        return BranchName.get();
    }

    public String getId() {
        return Id.get();
    }
    public String getMyDate() {
        return MyDate.get();
    }
    public String getDetail() {
        return Detail.get();
    }
    public boolean equal(CommitClass CC) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        if (this.Author.get().equals(CC.Author)
                && this.BranchName.get().equals(CC.branchName) 
                && this.Id.get().equals(String.valueOf(CC.Id))
                && this.MyDate.get().equals(ft.format(CC.MyDate))
                && this.Detail.get().equals(CC.Detail)) {
            return true;
        }
        return false;
    }
}
