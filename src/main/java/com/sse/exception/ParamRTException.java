package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-13 20:39
 */

public class ParamRTException extends RTExceptionBase {
    public ParamRTException() {
        super();
    }

    public ParamRTException(String message) {
        super(message);
    }

    public ParamRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamRTException(Throwable cause) {
        super(cause);
    }

    protected ParamRTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
