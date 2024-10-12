package com.zhedian.provide.system.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhedian.provide.system.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录用户身份权限
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 用户信息
     */
    private SysUser user;

    /**
     * 存储SpringSecurity所需要的权限信息的集合
     */
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    /**
     * 获取权限
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (ObjectUtils.isNotEmpty(permissions)) {
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }


    public LoginUser(SysUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
}
