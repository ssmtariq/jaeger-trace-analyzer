package com.ssmtariq.srlab.jtanalyzer;

public final class Constants {
    public static final String ES_SCHEMA = "http";
    public static final String ES_HOST = "192.168.45.2";
    public static final Integer ES_PORT = 9200;
    public static final String ES_INDEX = "jaeger-span-*";

    public static final String KEY_SPAN_ID = "spanID";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_REFERENCES = "references";
    public static final String KEY_PROCESS = "process";
    public static final String KEY_OPERATION_NAME = "operationName";
    public static final String KEY_SERVICE_NAME = "serviceName";
    public static final String[] FETCH_FIELDS = { "startTimeMillis", KEY_SPAN_ID, KEY_DURATION, KEY_REFERENCES, KEY_PROCESS, KEY_OPERATION_NAME };

    public static final String MATCH_FIELD = "spanID";
    public static final String[] MUST_MATCH = { "2a6ff9e9e58fa03d" };
    public static final String[] MUST_NOT_MATCH = { "21.211.33.63" };

    public static final String TIME_FIELD = "startTimeMillis";
//    @Value("${es.start-time}")
    public static final String START_TIME = Utility.dateToMilliseconds("2022/05/24 16:08:46"); //"1650312000000";
//    @Value("${es.end-time}")
    public static final String END_TIME = Utility.dateToMilliseconds("2025/05/26 10:18:56");// "1746561600000";//"2025-05-06T00:00:00";

    public static final String SERVICE_TRAVEL2 = "ts-travel2-service";
    public static final String SERVICE_TICKET_INFO = "ts-ticketinfo-service";
    public static final String SERVICE_BASIC = "ts-basic-service";
    public static final String SERVICE_PRICE = "ts-price-service";
    public static final String SERVICE_SEAT = "ts-seat-service";
    public static final String SERVICE_CONFIG = "ts-config-service";
    public static final String SERVICE_ROUTE = "ts-route-service";
    public static final String SERVICE_ORDER_OTHER = "ts-order-other-service";
    public static final String SERVICE_STATION = "ts-station-service";
    public static final String SERVICE_TRAIN = "ts-train-service";
    public static final String SERVICE_PRESERVE_OTHER = "ts-preserve-other-service";
    public static final String SERVICE_CONTACTS = "ts-contacts-service";
    public static final String SERVICE_ORDER = "ts-order-service";
    public static final String SERVICE_SECURITY = "ts-security-service";
    public static final String SERVICE_USER = "ts-user-service";
    public static final String SERVICE_NOTIFICATION = "ts-notification-service";
    public static final String SERVICE_FOOD = "ts-food-service";

    public static final Integer NUMBER_OF_SERVICE_COUNT = 14;
}
