/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import th.co.geniustree.nhso.drugcatalog.Constants;

/**
 *
 * @author moth
 */
public class DateUtils {

    public static Date parseDateWithOptionalTimeAndNoneLeneint(String dateString) throws IllegalArgumentException {
        Date date = null;
        SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(Constants.TMT_DATETIME_FORMAT, Locale.US);
        try {
            dateAndTimeFormat.setLenient(false);
            date = dateAndTimeFormat.parse(dateString);
        } catch (ParseException ex) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TMT_DATE_FORMAT, Locale.US);
            dateFormat.setLenient(false);
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException ex1) {
                throw new IllegalArgumentException(ex1);
            }
        }
        return date;
    }

    public static Date parseUSDate(String yyyyMMdd, String dateToParse) {
        SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(yyyyMMdd, Locale.US);
        try {
            return dateAndTimeFormat.parse(dateToParse);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String format(String pattern, Date value) {
        SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(pattern, Locale.US);
        return dateAndTimeFormat.format(value);
    }
}
