package CommonClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {

    private String Name;
    private String Password;
    private int Id;
    String Email;
    Date DateCreate;
    private int LastEventSee = 0;
    private List< String> MyFollow = new ArrayList<>();

    public void Update_Last_Event_See(int x) {
//        LastEventSee = x;
    }

    public int get_Last_Event_See() {
        return LastEventSee;
    }

    public void add_Follow(String ProjectName) {
        boolean found = false;
        for (String s : MyFollow) {
            if (s.equals(ProjectName)) {
                found = true;
            }
        }
        if (!found) {
            MyFollow.add(ProjectName);
        }
    }

    public void delete_Follow(String ProjectName) {
        List< String> temp = new ArrayList<>();
        for (String s : MyFollow) {
            if (!s.equals(ProjectName)) {
                temp.add(s);
            }
        }
        MyFollow = temp;
    }

    public List< String> getMyFollow() {
        return MyFollow;
    }

    public User(String Name, String Password) {
        this.Name = Name;
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setDateCreate(Date DateCreate) {
        this.DateCreate = DateCreate;
    }

    public String getEmail() {
        return Email;
    }

    public Date getDateCreate() {
        return DateCreate;
    }

}
