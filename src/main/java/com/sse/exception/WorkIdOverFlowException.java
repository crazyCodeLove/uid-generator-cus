package com.sse.exception;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 21:32
 */

public class WorkIdOverFlowException extends RTExceptionBase {
    public WorkIdOverFlowException() {
        super();
    }

    public WorkIdOverFlowException(String message) {
        super(message);
    }

    public WorkIdOverFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkIdOverFlowException(Throwable cause) {
        super(cause);
    }

    protected WorkIdOverFlowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
