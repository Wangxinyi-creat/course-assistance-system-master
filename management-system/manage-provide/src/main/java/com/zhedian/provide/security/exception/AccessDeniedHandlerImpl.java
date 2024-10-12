package com.zhedian.provide.security.exception;


import com.alibaba.fastjson2.JSON;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultCode;
import com.zhedian.common.util.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理器
 *
 * @author Administrator
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        CommonResult result = CommonResult.failed(ResultCode.UNAUTHORIZED);
        //响应给前端
        ResponseUtils.renderString(response, JSON.toJSONString(result));
    }
}
