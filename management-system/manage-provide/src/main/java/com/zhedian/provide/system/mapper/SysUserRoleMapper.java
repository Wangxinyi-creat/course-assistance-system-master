package com.zhedian.provide.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhedian.provide.system.entity.SysRole;
import com.zhedian.provide.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 账号角色表 Mapper 接口
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 通过用户名查询角色列表
     *
     * @param userName
     * @return
     */
    @Select("SELECT r.* FROM sys_user_role ur LEFT JOIN sys_role r ON ur.role_id=r.id WHERE ur.user_name=#{userName}")
    List<SysRole> getRoleByName(String userName);
}
