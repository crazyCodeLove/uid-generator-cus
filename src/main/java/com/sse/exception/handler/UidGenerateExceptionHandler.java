package com.sse.exception.handler;

import com.sse.exception.ParamRTException;
import com.sse.exception.RTExceptionBase;
import com.sse.model.ResponseResultHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 8:45
 */

@RestControllerAdvice
@Slf4j
public class UidGenerateExceptionHandler {

    /**
     * 参数校验错误，错误码统一为：1000，错误原因放到 message 中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ParamRTException.class)
    public ResponseResultHolder paramExceptionHandle(RTExceptionBase e) {
        log.error(e.getMessage());
        return ResponseResultHolder.builder().error(new ResponseResultHolder.ResponseError(1000, e.getMessage())).build();
    }

    /**
     * 运行时异常，打印错误信息，并返回500
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResultHolder RuntimeExceptHandler(RuntimeException e) {
        e.printStackTrace();
        return ResponseResultHolder.builder().error(new ResponseResultHolder.ResponseError(500, "server internal error, engineers are rushing to repair ...")).build();
    }

}
