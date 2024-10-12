package com.zhedian.provide.system.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.CustomExceptionResult;
import com.zhedian.provide.common.TokenService;
import com.zhedian.provide.system.entity.SysDept;
import com.zhedian.provide.system.entity.SysRole;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.mapper.SysUserMapper;
import com.zhedian.provide.system.mapper.SysUserRoleMapper;
import com.zhedian.provide.system.model.LoginUser;
import com.zhedian.provide.system.model.dto.LoginDto;
import com.zhedian.provide.system.model.vo.UserVo;
import com.zhedian.provide.system.service.SysDeptService;
import com.zhedian.provide.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 账号表 服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysDeptService deptService;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    private final static String password = "000000";

    @Override
    public CommonResult login(LoginDto loginDto) {
        //将获取的用户名和密码封装成Authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword());
        //通过AuthenticationManager的authenticate方法来进行用户认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果authenticate为空，则认证未通过 抛出异常提示
        if (Objects.isNull(authenticate)) {
            throw new CustomExceptionResult(500, "用户名或密码错误");
        }
        //authenticate内有LoginUser对象，通过getPrincipal获取(需要强转成LoginUser)
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        //认证通过了，使用userid生成token
        String token = tokenService.createToken(loginUser);
        //把token响应给前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", loginUser.getUser());
        map.put("permissions", loginUser.getPermissions());
        return CommonResult.success(map, "登陆成功");
    }

    @Override
    public Page<UserVo> selectPage(Integer pageSize, Integer pageNum, String userName, Integer deptId, String identity) {
        //分页查询
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        //条件构造
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(userName), SysUser::getUserName, userName)
                .eq(ObjectUtils.isNotNull(deptId), SysUser::getDeptId, deptId)
                .eq(ObjectUtils.isNotNull(identity), SysUser::getIdentity, identity)
                .ne(SysUser::getUserName, "admin");
        //查询
        Page<SysUser> sysUserPage = page(page, queryWrapper);
        //数据转换
        Page<UserVo> userVoPage = (Page<UserVo>) sysUserPage.convert(result -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(result, userVo);
            return userVo;
        });
        //查询关联角色 所属部门名称
        for (UserVo record : userVoPage.getRecords()) {
            //查询所属部门
            if (ObjectUtils.isNotNull(record.getDeptId())) {
                SysDept dept = deptService.getById(record.getDeptId());
                if (ObjectUtils.isNotNull(dept) && ObjectUtils.isNotNull(dept.getDeptName()))
                    record.setDeptName(dept.getDeptName());
            }
            //查询关联角色
            List<SysRole> roleList = sysUserRoleMapper.getRoleByName(record.getUserName());
            //获取角色列表中的id
            List<Integer> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
            //获取角色列表中的角色名称，并以逗号分割
            String roleNameS = roleList.stream().map(SysRole::getRoleName).collect(Collectors.joining("、"));
            //赋值
            if (ObjectUtils.isNotEmpty(roleIds)) record.setRoleId(roleIds);
            if (ObjectUtils.isNotEmpty(roleNameS)) record.setRoleName(roleNameS);
        }

        return userVoPage;
    }

    @Override
    public boolean updateUser(SysUser sysUser) {
        //通过id查询用户
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("id", sysUser.getId()));
        //判断是否修改密码
        if (!sysUser.getPassword().equals(user.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //如果传来密码为空  则设置密码为6个0
            String newPassword = ObjectUtils.isEmpty(sysUser.getPassword()) ? passwordEncoder.encode(password) : passwordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(newPassword);
        }
        return updateById(sysUser);
    }
}
