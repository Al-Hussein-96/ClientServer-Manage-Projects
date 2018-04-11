/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonRespone;

import CommonClass.Contributor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al-Hussein
 */
public class SendListContributors extends Respone implements Serializable {

    List<Contributor> list = new ArrayList<Contributor>();

    public SendListContributors(List<Contributor> list) {
        super(ResponeType.DONE);
        this.list = list;
    }

    public List<Contributor> getList() {
        return list;
    }

}
