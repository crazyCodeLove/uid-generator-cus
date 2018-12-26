package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-21 23:25
 */

public class UidBitAllocateException extends RTExceptionBase {
    public UidBitAllocateException() {
        super();
    }

    public UidBitAllocateException(String message) {
        super(message);
    }

    public UidBitAllocateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UidBitAllocateException(Throwable cause) {
        super(cause);
    }

    protected UidBitAllocateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
