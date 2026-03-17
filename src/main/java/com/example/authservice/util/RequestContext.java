package com.example.authservice.util;

import org.slf4j.MDC;

public final class RequestContext {

    public static final String REQUEST_ID = "requestId";

    private RequestContext() {
    }

    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }

    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID, requestId);
    }

    public static void clear() {
        MDC.remove(REQUEST_ID);
    }
}
