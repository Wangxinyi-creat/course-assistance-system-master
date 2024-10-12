package com.zhedian.provide.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhedian.provide.system.entity.SysUserRole;

import java.util.List;


/**
 * 账号角色表 服务类
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 新增/修改用户时  用户角色入表
     *
     * @param userName
     * @param roleIds
     */
    void addOrUpdateUserRole(String userName, List<Integer> roleIds);
}
