package com.ssmtariq.srlab.jtanalyzer;

public final class Constants {
    public static final String ES_SCHEMA = "http";
    public static final String ES_HOST = "192.168.174.226";
    public static final Integer ES_PORT = 9200;
    public static final String ES_INDEX = "jaeger-span-*";

    public static final String KEY_FLAGS = "flags";
    public static final String KEY_SPAN_ID = "spanID";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_REFERENCES = "references";
    public static final String KEY_PROCESS = "process";
    public static final String KEY_OPERATION_NAME = "operationName";
    public static final String KEY_SERVICE_NAME = "serviceName";
    public static final String[] FETCH_FIELDS = { "startTimeMillis", KEY_SPAN_ID, KEY_DURATION, KEY_REFERENCES, KEY_PROCESS, KEY_OPERATION_NAME };

    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String TIME_ZONE = "America/Detroit";
    public static final String TIME_FIELD = "startTimeMillis";
    public static final String START_TIME = Utility.dateToMilliseconds("2023/03/24 15:27:00"); //"1650312000000";
    public static final String END_TIME = Utility.dateToMilliseconds("2023/03/24 23:59:59");// "1746561600000";//"2025-05-06T00:00:00";
    public static final String MICRO_SERVICE_NAME = "ts-order-other-service";

    public static final Integer NUMBER_OF_CALLING_SERVICE_COUNT = 1;
    public static final Integer SUCCESS_REQUESTS_TO_RETRIEVE = 0;
}
