/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonRespone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Moaz
 */
public class SendMyFollowProjects extends Respone implements Serializable {

    List<String> MyFollowProjects = new ArrayList<>();

    public SendMyFollowProjects(List<String> mylist) {
        super(ResponeType.DONE);
        for (String temp : mylist) {
            this.MyFollowProjects.add(temp);
        }
    }

    public List<String> getMyFollowProjects() {
        return MyFollowProjects;
    }

}
