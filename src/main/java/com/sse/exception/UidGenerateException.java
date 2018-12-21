package com.sse.exception;

/**
 * 生成 uid 异常
 * @author pczhao
 * @email
 * @date 2018-12-21 8:43
 */

public class UidGenerateException extends RTException {
    public UidGenerateException() {
        super();
    }

    public UidGenerateException(String message) {
        super(message);
    }

    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}
