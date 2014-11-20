/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import th.co.geniustree.nhso.drugcatalog.Constants;

/**
 *
 * @author moth
 */
public class BigDecimalSpite {

    public BigDecimalSpite() {
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

    @Test(expected = NumberFormatException.class)
    public void parseWithComma() {
        new BigDecimal("9,606.00");
    }
    @Test(expected = NumberFormatException.class)
    public void parseWithDoubleParseDouble() {
        Double.parseDouble("9,606.00");
    }

    @Test
    public void parseWithCommaWithFormatter() throws ParseException {
        DecimalFormat formatter = new DecimalFormat(Constants.DEFAULT_DECIMAL_FORMAT);
        Assert.assertEquals(9606.05D, formatter.parse("9,606.05").doubleValue(), 0.00);
    }
    @Test
    public void parseWithCommaWithFormatterError() throws ParseException {
        DecimalFormat formatter = new DecimalFormat(Constants.DEFAULT_DECIMAL_FORMAT);
        Assert.assertEquals(9606.05D, formatter.parse("9,x606.05").doubleValue(), 0.00);
    }
    @Test
    public void stringReplace(){
        Assert.assertEquals("9606.00","9,606.00".replaceAll(",", ""));
    }
}
