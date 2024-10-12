package com.zhedian.provide.security.handle;

import com.alibaba.fastjson2.JSON;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.util.ResponseUtils;
import com.zhedian.provide.common.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 */
@Slf4j
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


    @Resource
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("自定义退出登录Handler... logout执行了");
        String uri = request.getRequestURI();
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtils.hasText(token)) {
            // 删除用户缓存记录
            tokenService.delLoginUser(token);
            ResponseUtils.renderString(response, JSON.toJSONString(CommonResult.success("退出成功")));
        } else {
            ResponseUtils.renderString(response, JSON.toJSONString(CommonResult.failed("当前用户没有登录")));
        }
        log.info("自定义退出登录Handler... logout执行结束");
    }
}
