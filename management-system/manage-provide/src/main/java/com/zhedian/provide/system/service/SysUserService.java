package com.zhedian.provide.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.model.dto.LoginDto;
import com.zhedian.provide.system.model.vo.UserVo;


/**
 * 账号表 服务类
 */
public interface SysUserService extends IService<SysUser> {


    /**
     * 登录
     *
     * @param loginDto 登录实体类
     * @return
     */
    CommonResult login(LoginDto loginDto);


    /**
     * 条件查询用户
     *
     * @param pageSize
     * @param pageNum
     * @param userName
     * @param deptId
     * @param identity
     * @return
     */
    Page<UserVo> selectPage(Integer pageSize, Integer pageNum, String userName, Integer deptId, String identity);

    /**
     * 修改用户
     *
     * @param sysUser
     * @return
     */
    boolean updateUser(SysUser sysUser);
}
