/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author Thanthathon
 */
public class FSNSplitter {

    private static final String ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN = "(\\w+[\\w\\s]*\\s)([\\d.]+\\s*[a-zA-Z]+/?[\\d.]*\\s*[a-zA-Z]*)";


    public static void getActiveIngredientFromFSN(TMTDrug drug) {
        if (drug.getType().equals(TMTDrug.Type.GP) || drug.getType().equals(TMTDrug.Type.TP) || drug.getType().equals(TMTDrug.Type.TPU)) {
            String fsn = drug.getFsn();
            Pattern p = Pattern.compile(ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher m = p.matcher(fsn);
            System.out.println("FSN : " + fsn);
            while (m.find()) {
                System.out.println("active + strength : " + m.group());
                System.out.println("active ingredient : " + m.group(1));
                System.out.println("         strength : " + m.group(2));
            }
        } else {
            System.out.println("FSN : " + drug.getFsn());
        }

    }

}
