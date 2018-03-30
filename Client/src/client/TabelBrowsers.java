package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TabelBrowsers {

    public int id;
    public boolean Type;
    public ImageView ImageIcon;
    public SimpleStringProperty Name;
    public SimpleStringProperty DataModified;
    public SimpleStringProperty Size;
    public SimpleStringProperty Description;

    public TabelBrowsers(String Name, boolean type, int id) {
        this.Name = new SimpleStringProperty(Name);
        this.Type = type;
        this.id = id;
        //     this.Description = new SimpleStringProperty(Description);
    }

    public String getName() {
        return Name.get();
    }

}
