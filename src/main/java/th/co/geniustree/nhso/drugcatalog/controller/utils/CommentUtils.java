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
     * @return <code>String</code> with convert line break to <code>&lt;br/&gt;</code> tag
     */
    public static String convertLineBreakToHtmlTag(String text) {
        if(text == null){
            return null;
        }
        return text.replaceAll("\r\n", "<br/>");
    }

    /**
     * Convert <code>String</code> for rendering on <code>p:inputTextarea</code>
     * @param text
     * @return 
     */
    public static String convertLineBreakToEscapeCharacter(String text) {
        if(text == null){
            return null;
        }
        return text.replaceAll("<br/>", "\r\n");
    }
}
