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
    public void givenFlagIsUThenUserDateEffective() {
        HospitalDrugExcelModel a = createModel("a","U","01/02/2014");
        HospitalDrugExcelModel b = createModel("a","U","01/02/2014");
        assertTrue(a.isEqual(b));
    }

    @Test
    public void givenFlagIsNotUThenUserDateChange() {
        HospitalDrugExcelModel a = createModel("a","E","01/01/2014");
        HospitalDrugExcelModel b = createModel("a","E","01/01/2014");
        assertTrue(a.isEqual(b));
    }

    public static HospitalDrugExcelModel createModel(String drugCode, String flag,String dateUpdate) {
        HospitalDrugExcelModel a = new HospitalDrugExcelModel();
        a.setHospDrugCode(drugCode);
        a.setUpdateFlag(flag);
        a.setDateEffective(dateUpdate);
        return a;
    }
}
