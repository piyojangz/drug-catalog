/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

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
public class HospitalDrugExcelModelTest {

    public HospitalDrugExcelModelTest() {
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
    public void shouldBeCheckOnlyDatePartNotIncludeTime() {
        HospitalDrugExcelModel a = createModel("a","U","01/01/2014 12:00","01/02/2014 13:00");
        HospitalDrugExcelModel b = createModel("a","U","11/11/2014 12:01","01/02/2014 13:01");
        assertTrue(a.isEqual(b));
    }

    @Test
    public void givenFlagIsUThenUserDateEffective() {
        HospitalDrugExcelModel a = createModel("a","U","01/01/2014","01/02/2014");
        HospitalDrugExcelModel b = createModel("a","U","11/11/2014","01/02/2014");
        assertTrue(a.isEqual(b));
    }

    @Test
    public void givenFlagIsNotUThenUserDateChange() {
        HospitalDrugExcelModel a = createModel("a","E","01/01/2014","01/02/2014");
        HospitalDrugExcelModel b = createModel("a","E","01/01/2014","11/12/2014");
        assertTrue(a.isEqual(b));
    }

    public static HospitalDrugExcelModel createModel(String drugCode, String flag, String dateChange, String dateEffective) {
        HospitalDrugExcelModel a = new HospitalDrugExcelModel();
        a.setHospDrugCode(drugCode);
        a.setUpdateFlag(flag);
        a.setDateChange(dateChange);
        a.setDateEffective(dateEffective);
        return a;
    }
}
