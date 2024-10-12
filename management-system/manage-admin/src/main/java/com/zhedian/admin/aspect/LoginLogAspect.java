package com.zhedian.admin.aspect;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zhedian.common.util.IpUtils;
import com.zhedian.provide.log.entity.SysLoginLog;
import com.zhedian.provide.log.service.ISysLoginLogService;
import com.zhedian.provide.system.model.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 登录日志 切面
 */

@Aspect
@Slf4j
@Component
public class LoginLogAspect {

    @Resource
    private ISysLoginLogService sysLoginLogService;

    @Resource
    private Ip2regionSearcher ip2regionSearcher;

    @Around("execution( * com.zhedian.admin.controller.system.SysUserController.login(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            //执行方法
            Object result = point.proceed();
            //保存日志
            saveLog(point);
            return result;
        } catch (Exception e) {
            //保存日志
            saveLog(point);
            throw e;
        }
    }

    private void saveLog(ProceedingJoinPoint point) {
        SysLoginLog loginLog = new SysLoginLog();

        //请求参数
        Object[] args = point.getArgs();
        try {
            LoginDto loginDto = (LoginDto) args[0];
            loginLog.setUserName(loginDto.getUserName());
        } catch (Exception e) {
            throw e;
        }


        //请求相关信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        //登录IP地址
        String ip = IpUtils.getIpAddr(request);
        loginLog.setIpaddr(ip);
        loginLog.setLoginLocation(ip2regionSearcher.getAddress(ip));

        //浏览器、系统相关信息
        UserAgent agent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
        loginLog.setBrowser(agent.getBrowser().toString());//浏览器类型
        loginLog.setOs(agent.getOs().toString());//系统类型

        //访问时间
        loginLog.setLoginTime(new Date());

        //保存DB
        sysLoginLogService.save(loginLog);
    }


}
