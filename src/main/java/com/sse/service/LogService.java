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
    public void infoRequest(HttpServletRequest request, ProceedingJoinPoint point) {
        StringBuilder sb = new StringBuilder(1024);
        /** 通用的请求数据 */
        sb.append("session ID:");
        sb.append(request.getSession().getId());
        sb.append("; url:");
        sb.append(request.getRequestURL());
        sb.append("; method:");
        sb.append(request.getMethod());
        sb.append("; query string:");
        sb.append(request.getQueryString());
        sb.append("; ip:");
        sb.append(IpUtil.getRequestIpAddr(request));
        sb.append("; params:");
        sb.append(JSON.toJSONString(request.getParameterMap()));

        /** 处理方法数据 */
        sb.append("; callClass:");
        sb.append(point.getTarget().getClass().getName());
        sb.append("; callMethod:");
        sb.append(point.getSignature().getName());
        sb.append("; args:");
        sb.append(Arrays.toString(point.getArgs()));
        log.info(sb.toString());
        System.out.println(new Date() + " " + sb);
    }

    @Async
    public void infoResponse(HttpServletRequest request, Object result, long requestStartTime) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("session ID:");
        sb.append(request.getSession().getId());
        sb.append("; result:");
        sb.append(result);
        sb.append("; cost time(ms):");
        sb.append(System.currentTimeMillis() - requestStartTime);
        log.info(sb.toString());
        System.out.print(DateTimeUtil.formatByDateTimeMsPattern(new Date()) + " " + sb.substring(0, sb.length() < 600 ? sb.length() : 600));
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
