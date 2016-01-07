/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thanthathon
 */
public class SummaryRequestMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryRequestMapper.class);

    public static List<SummaryRequest> mapAllToModel(List<Object[]> objects){
        List<SummaryRequest> summaryReqeust = new ArrayList<>();
        for(Object[] obj : objects){
            summaryReqeust.add(mapToModel(obj));
        }
        return summaryReqeust;
    }
    
    public static SummaryRequest mapToModel(Object[] object) {
        SummaryRequest summaryRequest = new SummaryRequest();
        int length = object.length;
        if (length == 10) {
            summaryRequest.setLastUpdate(stringToDate(object[0].toString()));
            summaryRequest.setHcode(object[1].toString());
            summaryRequest.setHname(object[2].toString());
            summaryRequest.setCountTMTNotNull(Integer.parseInt(object[3].toString()));
            summaryRequest.setCountTMTNull(Integer.parseInt(object[4].toString()));
            summaryRequest.setCountFlagA(Integer.parseInt(object[5].toString()));
            summaryRequest.setCountFlagE(Integer.parseInt(object[6].toString()));
            summaryRequest.setCountFlagD(Integer.parseInt(object[7].toString()));
            summaryRequest.setCountFlagU(Integer.parseInt(object[8].toString()));
            summaryRequest.setCountAll(Integer.parseInt(object[9].toString()));
        }
        return summaryRequest;
    }

    private static Date stringToDate(String dateText) {
        String[] dateTimeSplit = dateText.split(" ");
        
        String day = dateTimeSplit[0];
        String monthStr = dateTimeSplit[1];
        int date = Integer.parseInt(dateTimeSplit[2]);
        String time = dateTimeSplit[3];
        String timeZone = dateTimeSplit[4];
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
    
    private static int convertMonthStringToInt(String monthStr){
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
