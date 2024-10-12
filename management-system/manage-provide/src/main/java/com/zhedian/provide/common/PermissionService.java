package com.zhedian.provide.common;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhedian.provide.system.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 自定义权限实现
 */
@Service("perm")
public class PermissionService {

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermit(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        //获取当前登陆用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取当前登陆用户权限集合
        List<String> permissions = loginUser.getPermissions();
        //判断用户是否有此接口权限
        return permissions.contains(permission);
    }
}
