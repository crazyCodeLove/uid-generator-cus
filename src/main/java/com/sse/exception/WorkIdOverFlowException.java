package com.sse.exception;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 21:32
 */

public class WorkIdOverFlowException extends RTException {

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
}
