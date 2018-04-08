package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Al-Hussein
 */
public class TabelBranch {

    public StringProperty NameBranch;
    public StringProperty DataCreate;

    public TabelBranch(String NameBranch) {
        this.NameBranch = new SimpleStringProperty(NameBranch);
    }

    public StringProperty getNameBranch() {
        return NameBranch;
    }

    public StringProperty getDataCreate() {
        return DataCreate;
    }

}
