package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-22 14:36
 */

public class UidBatchSizeOverflowException extends ParamRTException {
    public UidBatchSizeOverflowException() {
        super();
    }

    public UidBatchSizeOverflowException(String message) {
        super(message);
    }

    public UidBatchSizeOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UidBatchSizeOverflowException(Throwable cause) {
        super(cause);
    }

    protected UidBatchSizeOverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
