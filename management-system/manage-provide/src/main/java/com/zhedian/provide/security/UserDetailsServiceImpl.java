package com.zhedian.provide.security;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhedian.common.domain.CustomExceptionResult;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.model.LoginUser;
import com.zhedian.provide.system.service.SysMenuService;
import com.zhedian.provide.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("--------查询登录账户--------" + username);
        //根据用户名查询用户信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, username);
        SysUser user = sysUserService.getOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(user)) {
            throw new CustomExceptionResult(403, "无此账号,请确认账号名是否正确");
        }
        //根据用户 查询操作权限
        List<String> permissionIdS = sysMenuService.getPermissionIdByUser(user.getUserName());
        //封装成UserDetails对象返回
        return new LoginUser(user, permissionIdS);
    }
}
