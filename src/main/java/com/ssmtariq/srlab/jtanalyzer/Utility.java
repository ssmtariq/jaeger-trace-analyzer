package com.ssmtariq.srlab.jtanalyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utility {
    /**
     * Convert custom date to milliseconds
     * @param iDate
     * @return
     */
    public static String dateToMilliseconds(String iDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
