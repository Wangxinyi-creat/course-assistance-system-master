package com.zhedian.provide.system.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.system.entity.SysUserRole;
import com.zhedian.provide.system.mapper.SysUserRoleMapper;
import com.zhedian.provide.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 账号角色表 服务实现类
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {


    @Override
    public void addOrUpdateUserRole(String userName, List<Integer> roleIds) {
        remove(new QueryWrapper<SysUserRole>().eq("user_name", userName));
        //如果roleIds为空 结束
        if (ObjectUtils.isEmpty(roleIds)) return;
        //for循环遍历入表
        for (Integer roleId : roleIds) {
            //社值
            SysUserRole userRole = new SysUserRole();
            userRole.setUserName(userName);
            userRole.setRoleId(roleId);
            //保存
            save(userRole);
        }
    }
}
