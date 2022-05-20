package com.ssmtariq.srlab.jtanalyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utility {
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    /**
     * Convert custom date to milliseconds
     * @param iDate
     * @return
     */
    public static String dateToMilliseconds(String iDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = dateFormat.parse(iDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime());
    }

    public static String getServiceNameShort(String name){
        return name.substring(3, (name.length()-8)).toUpperCase(Locale.ROOT);
    }
}
