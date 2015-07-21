/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Date;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;

/**
 *
 * @author Thanthathon
 */
public class EdNedMapper {

    public static EdNed mapToModel(Object[] obj) {
        System.out.println("length " + obj.length);
        System.out.println("obj[0] = " + obj[0]);

        if (obj.length == 1) {
        } else if (obj.length == 4) {

            Date date = DateUtils.parseUSDate("dd MMM yyyy",obj[2].toString());
            EdNed edNed = new EdNed(obj[0].toString(), obj[1].toString(), date);
            edNed.setStatus(obj[3].toString());

            return edNed;
        }
        return null;
    }
}
