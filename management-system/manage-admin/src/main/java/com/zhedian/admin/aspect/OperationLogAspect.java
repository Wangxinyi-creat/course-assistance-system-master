package com.zhedian.admin.aspect;


import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.util.IpUtils;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.log.entity.SysOperateLog;
import com.zhedian.provide.log.service.ISysOperateLogService;
import com.zhedian.provide.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Aspect
@Slf4j
@Component
public class OperationLogAspect {


    @Resource
    private ISysOperateLogService sysOperateLogService;


    @Pointcut("@annotation(com.zhedian.common.annotation.LogOperation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            //执行方法
            Object result = point.proceed();
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, result);
            return result;
        } catch (Exception e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, null);
            throw e;
        }
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time, Object result) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        LogOperation annotation = method.getAnnotation(LogOperation.class);

        SysOperateLog operateLog = new SysOperateLog();

        if (annotation != null) {
            //注解上的描述
            operateLog.setMethodName(annotation.value());//方法名称
        }

        //登录用户信息
        SysUser user = AuthService.getUser();
        operateLog.setUserName(user.getUserName());//操作人
        operateLog.setCreateTime(new Date());//操作时间
        operateLog.setExpendTime((int) time + "ms");//消耗时间

        //请求相关信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        operateLog.setReqIp(IpUtils.getIpAddr(request));//IP
        operateLog.setReqInterface(request.getRequestURI());//请求接口
        operateLog.setReqType(request.getMethod());//请求方式

        //浏览器、系统相关信息
        UserAgent agent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        operateLog.setBrowser(agent.getBrowser().toString());//浏览器类型
        operateLog.setOs(agent.getOs().toString());//系统类型

        //请求参数 操作状态 返回参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args[0]);
            operateLog.setReqParam(params);//请求参数
        } catch (Exception e) {
            throw e;
        }
        if (ObjectUtils.isNotNull(result)) {
            operateLog.setStatus((((CommonResult) result).getCode() == 200 ? 1 : 0));//操作状态
            operateLog.setJsonResult(JSON.toJSONString(result));//返回参数
        } else {
            operateLog.setStatus(2);
        }
        //保存到DB
        sysOperateLogService.save(operateLog);
    }
}
