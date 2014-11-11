/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author moth
 */
public class GuavaSplitSpite {

    public GuavaSplitSpite() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void splitByCharMatcher() {
        List<String> splitToList = Splitter.on(CharMatcher.anyOf("=,")).trimResults().omitEmptyStrings().splitToList("=M4,C1");
        assertTrue(splitToList.size() == 2);
    }
}
