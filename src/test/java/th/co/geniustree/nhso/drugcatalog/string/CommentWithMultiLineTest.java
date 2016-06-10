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
    public void englishNewLineMustUseSeparator() {
        String input = "Hello world.\r\nThis is a new line.";
        String output = CommentUtils.convertLineBreakToSeparator(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("\r", "\n");

        Assertions.assertThat(output)
                .contains(", ")
                .doesNotContain("\r")
                .doesNotContain("\n");
    }
    
    @Test
    public void thaiNewLineMustUseSeparator() {
        String input = "สวัสดี\r\nนี่คือบรรทัดใหม่";
        String output = CommentUtils.convertLineBreakToSeparator(input);
        System.out.println("output : " + output);

        Assertions.assertThat(input)
                .contains("\r", "\n");

        Assertions.assertThat(output)
                .contains(", ")
                .doesNotContain("\r")
                .doesNotContain("\n");
    }
    
}
