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
public class CommentUtils {

    /**
     * Convert <code>String</code> for save in database
     * @param text input string with line break
     * @return <code>String</code> with convert line break to ", "
     */
    public static String convertLineBreakToSeparator(String text) {
        if(text == null){
            return null;
        }
        return text.replaceAll("\r\n", ", ");
    }

}
