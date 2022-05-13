package com.ssmtariq.srlab.jtanalyzer;

public final class Constants {
    public static final String ES_SCHEMA = "http";
    public static final String ES_HOST = "192.168.45.3";
    public static final Integer ES_PORT = 9200;
    public static final String ES_INDEX = "jaeger-span-*";

    public static final String KEY_SPAN_ID = "spanID";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_REFERENCES = "references";
    public static final String KEY_PROCESS = "process";
    public static final String KEY_SERVICE_NAME = "serviceName";
    public static final String[] FETCH_FIELDS = { "startTimeMillis", KEY_SPAN_ID, KEY_DURATION, KEY_REFERENCES, KEY_PROCESS };

    public static final String MATCH_FIELD = "spanID";
    public static final String[] MUST_MATCH = { "2a6ff9e9e58fa03d" };
    public static final String[] MUST_NOT_MATCH = { "21.211.33.63" };

    public static final String TIME_FIELD = "startTimeMillis";
//    @Value("${es.start-time}")
    public static final String START_TIME = Utility.dateToMilliseconds("2022/05/09 16:00:00"); //"1650312000000";
//    @Value("${es.end-time}")
    public static final String END_TIME = Utility.dateToMilliseconds("2025/05/06 00:00:00");// "1746561600000";//"2025-05-06T00:00:00";

}
