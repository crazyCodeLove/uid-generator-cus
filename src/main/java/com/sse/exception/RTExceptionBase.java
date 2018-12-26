package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-13 20:38
 */

public class RTExceptionBase extends RuntimeException {

    public RTExceptionBase() {
        super();
    }

    public RTExceptionBase(String message) {
        super(message);
    }

    public RTExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    public RTExceptionBase(Throwable cause) {
        super(cause);
    }

    protected RTExceptionBase(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
