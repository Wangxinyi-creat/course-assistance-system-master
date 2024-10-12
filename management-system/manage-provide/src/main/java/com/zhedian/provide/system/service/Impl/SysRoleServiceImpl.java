package com.zhedian.provide.system.service.Impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.system.entity.SysRole;
import com.zhedian.provide.system.mapper.SysRoleMapper;
import com.zhedian.provide.system.service.SysRoleService;
import org.springframework.stereotype.Service;


/**
 * 角色表 服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
