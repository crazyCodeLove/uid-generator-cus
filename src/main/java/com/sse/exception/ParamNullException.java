package com.sse.exception;

/**
 * @author pczhao
 * @email
 * @date 2018-12-26 12:30
 */

public class ParamNullException extends ParamRTException {
    public ParamNullException() {
        super();
    }

    public ParamNullException(String message) {
        super(message);
    }

    public ParamNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamNullException(Throwable cause) {
        super(cause);
    }

    protected ParamNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
