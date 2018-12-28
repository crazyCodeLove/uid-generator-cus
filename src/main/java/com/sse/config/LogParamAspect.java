package com.sse.config;

import com.alibaba.fastjson.JSON;
import com.sse.model.RequestParamBase;
import com.sse.model.RequestParamHolder;
import com.sse.service.LogService;
import com.sse.util.IpUtil;
import com.sse.util.ValidateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-04 22:14
 */

@Configuration
@Aspect
public class LogParamAspect {

    @Autowired
    private LogService logService;

    /**
     * 只记录请求的参数
     */
    @Pointcut("execution(* com.sse.uid.controller..*.* (..)) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerPoint() {
    }

    @Around("controllerPoint()")
    public Object aroundController(ProceedingJoinPoint point) throws Throwable {
        Date requestTime = new Date();
        String ruid = UUID.randomUUID().toString();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        StringBuilder sb = new StringBuilder(1024);
        /** 通用的请求数据 */
        sb.append("request ID:");
        sb.append(ruid);
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
        logService.infoRequest(sb, requestTime);
        Object result = null;
        try {
            /** 对参数进行统一校验 */
            validParamInAsp(point.getArgs());
            result = point.proceed();
        } finally {
            /** 记录响应 */
            logService.infoResponse(ruid, result, new Date(), requestTime);
        }
        return result;
    }

    /**
     * 对参数使用 hibernate validator 进行校验
     *
     * @param params
     */
    public void validParamInAsp(Object[] params) {
        if (params != null) {
            for (Object obj : params) {
                if (obj instanceof RequestParamHolder) {
                    Object param = ((RequestParamHolder) obj).getParam();
                    if (param != null) {
                        ValidateUtil.validate(param);
                        if (param instanceof RequestParamBase) {
                            ((RequestParamBase) param).validParamInParam();
                        }
                    }
                }
            }
        }
    }


}
