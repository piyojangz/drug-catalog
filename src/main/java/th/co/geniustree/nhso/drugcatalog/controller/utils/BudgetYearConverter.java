/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Thanthathon
 */
public class BudgetYearConverter {

    private static final String YEAR_PATTERN = "yyyy";

    public static int dateToBudgetYear(Date date) {
        Integer budgetYear = Integer.parseInt(DateUtils.format(YEAR_PATTERN, date));
        
        Date budgetDate = new GregorianCalendar(budgetYear, 9, 1).getTime();
        budgetYear = convertToBEYear(budgetYear);
        
        if (date.after(budgetDate)) {
            budgetYear += 1;
        }
        return budgetYear;
    }

    public static Date budgetYearToDate(Integer budgetYear) {
        return new GregorianCalendar(budgetYear, 9, 1).getTime();
    }

    public static Date getCurrentBudgetDate() {
        return budgetYearToDate(dateToBudgetYear(new Date()));
    }

    /**
     * get Thai year (Buddhist Era)
     * example <br/>
     *   input 2015 -> output 2558<br/>
     *   input 2550 -> output 2550 because now C.E. year is 2015
     * @param year
     * @return B.E year
     */
    public static Integer convertToBEYear(Integer year) {
        Integer currentYear = Integer.parseInt(DateUtils.format(YEAR_PATTERN, new Date()));
        if (year - currentYear <= 0) {
            year += 543;
        }
        return year;
    }
    
    /**
     * get Christian Era year
     * example <br/>
     *   input 2015 -> output 2015<br/>
     *   input 2550 -> output 2007 because now C.E. year is 2015
     * @param year
     * @return B.E year
     */
    public static Integer convertToCEYear(Integer year){
        Integer currentYear = Integer.parseInt(DateUtils.format(YEAR_PATTERN, new Date()));
        if (year - currentYear >= 0) {
            year -= 543;
        }
        return year;
    }
}
