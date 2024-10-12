package com.zhedian.provide.common;


import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取登录用户信息服务类
 */
public class AuthService {

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static SysUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser();
    }
}
