/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

/**
 *
 * @author thanthathon.b
 */
public class NDC24Utils {

    public static String separateWithStructure(String ndc24, String separateWith) {
        if (!ndc24.matches("\\d{24}")) {
            throw new IllegalArgumentException("NDC24 must with 24 number");
        }
        return new StringBuilder().append(ndc24.substring(0, 1)).append(separateWith)
                .append(ndc24.subSequence(1, 11)).append(separateWith)
                .append(ndc24.substring(11, 16)).append(separateWith)
                .append(ndc24.substring(16, 19)).append(separateWith)
                .append(ndc24.substring(19, 24)).toString();
    }
    
    public static String separateWithStructure(String ndc24) {
        return separateWithStructure(ndc24," ");
    }
}
