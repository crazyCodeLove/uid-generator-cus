package com.sse.service;

import com.alibaba.fastjson.JSON;
import com.sse.util.DateTimeUtil;
import com.sse.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * @author pczhao
 * @email
 * @date 2018-12-28 9:09
 */

@Service
@Slf4j
public class LogService {

    @Async
    public void debug(String s) {
        log.debug(s);
    }

    @Async
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Async
    public void infoRequest(StringBuilder requestInfo, Date requestTime) {
        log.info(requestInfo.toString());
        System.out.println(DateTimeUtil.formatByDateTimeMsPattern(requestTime) + " " + requestInfo);
    }

    @Async
    public void infoResponse(String requestId, Object result, Date responseTime, Date requestTime) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("request ID:");
        sb.append(requestId);
        sb.append("; cost time(ms):");
        sb.append(responseTime.getTime() - requestTime.getTime());
        sb.append("; result:");
        sb.append(result);
        log.info(sb.toString());
        System.out.println(DateTimeUtil.formatByDateTimeMsPattern(responseTime) + " " + sb.substring(0, sb.length() < 400 ? sb.length() : 400));
    }

    @Async
    public void info(String s) {
        log.info(s);
    }

    @Async
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Async
    public void warn(String s) {
        log.warn(s);
    }

    @Async
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Async
    public void error(String s) {
        log.error(s);
    }

    @Async
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }
}
