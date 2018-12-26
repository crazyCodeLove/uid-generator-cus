package com.sse.exception;

/**
 * @author pczhao
 * @email
 * @date 2018-12-26 13:16
 */

public class ParseDateTimeException extends RTExceptionBase {
    public ParseDateTimeException() {
        super();
    }

    public ParseDateTimeException(String message) {
        super(message);
    }

    public ParseDateTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseDateTimeException(Throwable cause) {
        super(cause);
    }

    protected ParseDateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
