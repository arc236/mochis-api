package com.ace.mochis.util;

import org.slf4j.MDC;

import java.util.UUID;

public class RequestId {
    public static final String REQUEST_ID_HEADER = "X-REQUEST-ID";
    public static final String POSTMAN_TOKEN_HEADER ="Postman-Token";
    public static final String REQUEST_ID_PARAM = "RequestId";
    public static final String USER_NAME_PARAM = "UserName";
    public static final String CRON_USER_NAME = "CRON:";
    
    private static final ThreadLocal<String> id = new ThreadLocal<String>();

    private static void setId(String requestId) {
        id.set(requestId);
    }

    public static String getId() {
        return id.get();
    }

    public static void start(String userName) {
        String requestId = UUID.randomUUID().toString();
        setId(requestId);
        MDC.put( RequestId.REQUEST_ID_PARAM, requestId );
        MDC.put( RequestId.USER_NAME_PARAM, userName );
    }

    public static void stop() {
        MDC.remove( RequestId.REQUEST_ID_PARAM );
        MDC.remove( RequestId.USER_NAME_PARAM);
    }
}