package com.sse.exception;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:52
 */

public class RTException extends RuntimeException {
    public RTException() {
        super();
    }

    public RTException(String message) {
        super(message);
    }

    public RTException(String message, Throwable cause) {
        super(message, cause);
    }

    public RTException(Throwable cause) {
        super(cause);
    }
}
