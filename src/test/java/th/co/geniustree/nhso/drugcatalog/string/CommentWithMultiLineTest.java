/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.string;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import th.co.geniustree.nhso.drugcatalog.controller.utils.CommentUtils;

/**
 *
 * @author thanthathon.b
 */
public class CommentWithMultiLineTest {

    @Test
    public void englishNewLineMustConvertToHtmlTag() {
        String input = "Hello world.\r\nThis is a new line.";
        String output = CommentUtils.convertLineBreakToHtmlTag(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("\r", "\n");

        Assertions.assertThat(output)
                .contains("<br/>")
                .doesNotContain("\r")
                .doesNotContain("\n");
    }
    
    @Test
    public void englishNewLineMustConvertToEscapeCharacter() {
        String input = "Hello world.<br/>This is a new line.";
        String output = CommentUtils.convertLineBreakToEscapeCharacter(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("<br/>");

        Assertions.assertThat(output)
                .contains("\r","\n")
                .doesNotContain("<br/>");
    }

    @Test
    public void thaiNewLineMustConvertToHtmlTag() {
        String input = "สวัสดี\r\nนี่คือบรรทัดใหม่";
        String output = CommentUtils.convertLineBreakToHtmlTag(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("\r", "\n");

        Assertions.assertThat(output)
                .contains("<br/>")
                .doesNotContain("\r")
                .doesNotContain("\n");
    }
    
    @Test
    public void thaiNewLineMustConvertToEscapeCharacter() {
        String input = "สวัสดี<br/>นี่คือบรรทัดใหม่";
        String output = CommentUtils.convertLineBreakToEscapeCharacter(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("<br/>");

        Assertions.assertThat(output)
                .contains("\r","\n")
                .doesNotContain("<br/>");
    }
}
