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

/**
 *
 * @author moth
 */
public class DateUtils {

    public static Date parseDateWithOptionalTimeAndNoneLeneint(String dateString) throws IllegalArgumentException {
        Date date = null;
        SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        try {
            dateAndTimeFormat.setLenient(false);
            date = dateAndTimeFormat.parse(dateString);
        } catch (ParseException ex) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            dateFormat.setLenient(false);
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException ex1) {
                throw new IllegalArgumentException(ex1);
            }
        }
        return date;
    }
}
