/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;

/**
 *
 * @author Thanthathon
 */
public class EdNedMapper {

    private static final Logger LOG = LoggerFactory.getLogger(EdNedMapper.class);

    /**
     * select a first ed_status by min dateIn and mapping to entity class
     *
     * @param objList - list of result
     * @return edNed entity - a first element
     */
    public static TMTEdNed mapToModelAndGetFirst(List<Object[]> objList) {
//        if (objList.isEmpty()) {
//            return null;
//        }
//        Object[] obj = objList.get(0);
//        if (obj.length != 4) {
//            return null;
//        }
//        Date date = stringToDate(obj[2].toString());
//        EdNed edNed = new EdNed(obj[0].toString(), obj[1].toString(), date);
//        edNed.setStatus(obj[3].toString());

//        return edNed;
        return null;
    }

    private static Date stringToDate(String dateText) {
        String[] dateTimeSplit = dateText.split(" ");

//        String day = dateTimeSplit[0];
        String monthStr = dateTimeSplit[1];
        int date = Integer.parseInt(dateTimeSplit[2]);
        String time = dateTimeSplit[3];
//        String timeZone = dateTimeSplit[4];
        int year = Integer.parseInt(dateTimeSplit[5]);

        int month = convertMonthStringToInt(monthStr);

        String[] timeSplit = time.split(":");
        int hourOfDay = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);
        int second = Integer.parseInt(timeSplit[2]);

        Calendar calendar = new GregorianCalendar(year, month, date, hourOfDay, minute, second);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
        return calendar.getTime();
    }

    private static int convertMonthStringToInt(String monthStr) {
        Date d = null;
        try {
            d = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthStr);
        } catch (ParseException ex) {
            LOG.error(ex.getMessage());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.MONTH);
    }
}
