package com.zhedian.provide.security.filter;

import com.alibaba.fastjson2.JSON;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultCode;
import com.zhedian.common.util.ResponseUtils;
import com.zhedian.provide.common.TokenService;
import com.zhedian.provide.system.model.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * 定义Jwt认证过滤器
 * 解析token
 * 获取其中的userid
 * 从redis中获取用户信息(使用userid在redis中获取对应的LoginUser对象)
 * 存入SecurityContextHolder
 *
 * @author Administrator
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter implements HandlerInterceptor {


    @Resource
    private TokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token   返回LoginUser
        LoginUser loginUser = tokenService.parseToken(token);
        //如果获取不到
        if (Objects.isNull(loginUser)) {
            //说明登录过期  提示重新登录
            CommonResult commonResult = CommonResult.failed(ResultCode.FORBIDDEN);
            ResponseUtils.renderString(response, JSON.toJSONString(commonResult));
            return;
        }
        //将用户信息封装成Authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        //将封装好的用户信息存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }

}
