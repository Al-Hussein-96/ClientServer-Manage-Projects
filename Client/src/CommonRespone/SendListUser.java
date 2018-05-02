/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonRespone;

import CommonClass.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al-Hussein
 */
public class SendListUser extends Respone{
    List<User> listUser;
    public SendListUser(List<User> listUser) {
        super(ResponeType.DONE);
        this.listUser = new ArrayList<>(listUser);
    }

    public List<User> getListUser() {
        return listUser;
    }
    
    
    
}
