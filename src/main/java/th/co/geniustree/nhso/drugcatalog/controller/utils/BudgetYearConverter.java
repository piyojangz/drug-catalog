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
        if (date.after(budgetDate)) {
            budgetYear += 1;
        }
        return budgetYear;
    }

    public static Date budgetYearToDate(Integer budgetYear) {
        return new GregorianCalendar(budgetYear, 9, 1).getTime();
    }
}
