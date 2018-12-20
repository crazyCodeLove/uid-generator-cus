package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:52
 */

public class UidGenerateException extends RuntimeException {
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
