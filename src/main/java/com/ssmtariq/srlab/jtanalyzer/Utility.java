package com.ssmtariq.srlab.jtanalyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

//    public static Map<String, Double> sortByValue(Map<String, Double> map) {
//        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
//        list.sort(Map.Entry.comparingByValue());
//        Collections.reverse(list);
//
//        Map<String, Double> result = new LinkedHashMap<>();
//        for (Map.Entry<String, Double> entry : list) {
//            result.put(entry.getKey(), entry.getValue());
//        }
//
//        return result;
//    }

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
