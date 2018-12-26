package com.sse.exception;

/**
 * 生成 uid 异常
 * @author pczhao
 * @email
 * @date 2018-12-21 8:43
 */

public class UidGenerateException extends RTExceptionBase {
    public UidGenerateException() {
        super();
    }

    public UidGenerateException(String message) {
        super(message);
    }

    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UidGenerateException(Throwable cause) {
        super(cause);
    }

    protected UidGenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
