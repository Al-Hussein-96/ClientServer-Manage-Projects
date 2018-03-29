package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TabelBrowsers {

    public ImageView ImageIcon;
    public SimpleStringProperty Name;
    public SimpleStringProperty DataModified;
    public SimpleStringProperty Size;
    public SimpleStringProperty Description;

    public TabelBrowsers(String Name) {
        this.Name = new SimpleStringProperty(Name);
        //     this.Description = new SimpleStringProperty(Description);
    }

    public String getName() {
        return Name.get();
    }


}
