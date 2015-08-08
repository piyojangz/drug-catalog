/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;

/**
 *
 * @author Thanthathon
 */
public class BudgetYearTest {

    public BudgetYearTest() {
    }

    
    @Test
    public void budhhistEraTest() {
        Date date = new GregorianCalendar(2015, 9, 1).getTime();
        int year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2558, year);
        
        date = new GregorianCalendar(2556, 9, 1).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2556, year);
        
        date = new GregorianCalendar(2557, 9, 2).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2558, year);
        
        date = new GregorianCalendar(2014, 7, 25).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2557, year);
        
        date = new GregorianCalendar(2011, 11, 30).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2555, year);
        
        date = new GregorianCalendar(2099, 5, 5).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2099, year);
        
        date = new GregorianCalendar(2699, 5, 5).getTime();
        year = BudgetYearConverter.dateToBudgetYear(date);
        assertEquals(2699, year);
        
    }
}
