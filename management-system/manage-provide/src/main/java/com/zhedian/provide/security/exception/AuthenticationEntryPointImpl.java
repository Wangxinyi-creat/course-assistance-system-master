package com.zhedian.provide.security.exception;

import com.alibaba.fastjson2.JSON;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultCode;
import com.zhedian.common.util.ResponseUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器(登录....)
 *
 * @author Administrator
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        CommonResult result = null;
        //BadCredentialsException :当用户名或者密码错误抛出此异常
        //DisabledException:账号被封禁

        //     LoginUser下对应的四个异常机制
        //AccountExpiredException:账号过期异常--LoginUser--isAccountNonExpired
        //LockedException:锁定异常--LoginUser--isAccountNonLocked
        //CredentialsExpiredException:凭据过期异常--LoginUser--isCredentialsNonExpired
        //InsufficientAuthenticationException :权限不足或需要登录时抛出此异常--LoginUser--isEnabled

        if (authException instanceof BadCredentialsException) {
            //用户名或密码错误
            result = CommonResult.failed(ResultCode.FAILED.getCode(), "用户名或密码错误,请检查是否填写正确");
        } else if (authException instanceof InsufficientAuthenticationException) {
            //未登录
            result = CommonResult.failed(ResultCode.FORBIDDEN.getCode(), "请先登录");
        } else if (authException instanceof DisabledException) {
            //账号锁定
            result = CommonResult.failed(ResultCode.FAILED.getCode(), "此账号已被禁用,请联系管理员");
        } else {
            //未授权
            result = CommonResult.failed(ResultCode.UNAUTHORIZED);
        }
        //响应给前端
        ResponseUtils.renderString(response, JSON.toJSONString(result));
    }
}
