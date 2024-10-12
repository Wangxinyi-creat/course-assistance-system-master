package com.zhedian.admin.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.entity.SysUserRole;
import com.zhedian.provide.system.model.dto.LoginDto;
import com.zhedian.provide.system.model.dto.ModifyPwd;
import com.zhedian.provide.system.model.dto.UserDto;
import com.zhedian.provide.system.model.vo.UserVo;
import com.zhedian.provide.system.service.SysUserRoleService;
import com.zhedian.provide.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 用户表 接口
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    private final static String password = "000000";

    @ApiOperation("验证码获取")
    @GetMapping("/captcha")
    public CommonResult defaultKaptcha() throws Exception {
        //生成验证码对象,三个参数分别是宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
        //设置验证码的字符类型为数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 设置内置字体
        captcha.setCharType(Captcha.FONT_1);
        //获取验证码内容
        String text = captcha.text().toLowerCase();
        //输出base64图片数据
        String base64 = captcha.toBase64();
        //转为map
        HashMap<String, String> map = new HashMap<>();
        map.put("imgBase64", base64);
        map.put("imgText", text);
        return CommonResult.success(map, "true");
    }

    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody LoginDto loginDto) {
        return sysUserService.login(loginDto);
    }

    @ApiOperation("查询用户")
    @PreAuthorize("@perm.hasPermit('system:user:get')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<UserVo>> getUser(@RequestParam(required = false) String userName,
                                                    @RequestParam(required = false) Integer deptId,
                                                    @RequestParam(required = false) String identity,
                                                    @RequestParam Integer pageSize,
                                                    @RequestParam Integer pageNum) {
        Page<UserVo> userVoPage = sysUserService.selectPage(pageSize, pageNum, userName, deptId, identity);
        return CommonResult.success(ResultPage.restPage(userVoPage));
    }

    @Transactional
    @ApiOperation("添加用户")
    @LogOperation("添加用户")
    @PreAuthorize("@perm.hasPermit('system:user:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addUser(@RequestBody UserDto userDto) {
        //SysUser表操作
        SysUser sysUser = userDto.getUser();
        SysUser user = AuthService.getUser();
        sysUser.setCreateBy(user.getUserName());
        sysUser.setCreateTime(new Date());
        //加密密码   若前端密码为空  则为默认密码6个0
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = ObjectUtils.isEmpty(sysUser.getPassword()) ? passwordEncoder.encode(password) : passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(newPassword);
        //userName不可重复   数据库已加唯一索引  所以仅需判断是否添加成功
        if (sysUserService.save(sysUser)) {
            //处理用户角色
            sysUserRoleService.addOrUpdateUserRole(sysUser.getUserName(), userDto.getRoleSign());
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Transactional
    @ApiOperation("注册用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult register(@RequestBody UserDto userDto) {
        //SysUser表操作
        SysUser sysUser = userDto.getUser();
        sysUser.setCreateBy(sysUser.getUserName());
        sysUser.setCreateTime(new Date());
        //加密密码   若前端密码为空  则为默认密码6个0
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = ObjectUtils.isEmpty(sysUser.getPassword()) ? passwordEncoder.encode(password) : passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(newPassword);
        //userName不可重复   数据库已加唯一索引  所以仅需判断是否添加成功
        if (sysUserService.save(sysUser)) {
            //处理用户角色
            sysUserRoleService.addOrUpdateUserRole(sysUser.getUserName(), userDto.getRoleSign());
            return CommonResult.success("注册成功");
        }
        return CommonResult.failed("注册失败");
    }

    @Transactional
    @ApiOperation("修改用户")
    @LogOperation("修改用户")
    @PreAuthorize("@perm.hasPermit('system:user:update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateUser(@RequestBody UserDto userDto) {
        //SysUser表操作
        SysUser sysUser = userDto.getUser();
        SysUser user = AuthService.getUser();
        sysUser.setUpdateBy(user.getUserName());
        sysUser.setUpdateTime(new Date());
        //修改
        if (sysUserService.updateUser(sysUser)) {
            //处理用户角色
            sysUserRoleService.addOrUpdateUserRole(sysUser.getUserName(), userDto.getRoleSign());
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除用户")
    @LogOperation("删除用户")
    @PreAuthorize("@perm.hasPermit('system:user:delete')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteUser(@PathVariable("id") Integer id) {
        return CommonResult.success(sysUserService.removeById(id));
    }

    @ApiOperation("修改用户状态-是否封禁")
    @LogOperation("修改用户状态-是否封禁")
    @PreAuthorize("@perm.hasPermit('system:user:update')")
    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.POST)
    public CommonResult changeStatus(@PathVariable("id") Integer id,
                                     @PathVariable("status") Boolean status) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<SysUser>();
        updateWrapper.set("status", status)
                .set("update_by", AuthService.getUser().getUserName())
                .set("update_time", new Date())
                .eq("id", id);
        if (sysUserService.update(updateWrapper)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("修改个人信息")
    @LogOperation("修改个人信息")
    @RequestMapping(value = "/updatePersonal", method = RequestMethod.POST)
    public CommonResult updatePersonal(@RequestBody SysUser sysUser) {
        //设置修改人  修改时间
        sysUser.setUpdateBy(AuthService.getUser().getUserName());
        sysUser.setUpdateTime(new Date());
        //修改
        if (sysUserService.updateById(sysUser)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("修改密码")
    @LogOperation("修改个人密码")
    @RequestMapping(value = "/updatePwd/{id}", method = RequestMethod.POST)
    public CommonResult updatePwd(@PathVariable("id") Integer id,
                                  @RequestBody ModifyPwd modifyPwd) {
        //通过id查询用户
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("id", id));
        //判断是否密码是否正确
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(modifyPwd.getOldPassword(), user.getPassword())) {
            return CommonResult.failed("旧密码有误,请重新输入");
        }
        //判断两次新密码是否一致
        if (!Objects.equals(modifyPwd.getNewPassword1(), modifyPwd.getNewPassword2())) {
            return CommonResult.failed("两次密码不一致,请核对后在提交");
        }
        //密码加密
        String newPassword = passwordEncoder.encode(modifyPwd.getNewPassword2());
        //条件构造
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getPassword, newPassword).eq(SysUser::getId, id);
        return CommonResult.success(sysUserService.update(updateWrapper));
    }

    @ApiOperation("By角色id查询用户")
    @RequestMapping(value = "getUserByRoleId", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysUser>> getUserByRoleId(@RequestParam Integer roleId,
                                                             @RequestParam(required = false) String userName,
                                                             @RequestParam Integer pageSize,
                                                             @RequestParam Integer pageNum) {
        //分页查询---条件构造
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(userName), SysUser::getUserName, userName);
        //获取用户
        List<SysUserRole> userRoleList = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("role_id", roleId));
        //若为空-->构造一个不存在的条件进行查询
        if (ObjectUtils.isNotEmpty(userRoleList)) {
            List<String> userNameList = userRoleList.stream().map(SysUserRole::getUserName).collect(Collectors.toList());
            queryWrapper.in(SysUser::getUserName, userNameList);
        } else {
            queryWrapper.eq(SysUser::getId, 0);
        }
        //查询
        Page<SysUser> sysUserPage = sysUserService.page(page, queryWrapper);
        return CommonResult.success(ResultPage.restPage(sysUserPage));
    }

    @ApiOperation("取消角色授权")
    @RequestMapping(value = "cancelUserRole", method = RequestMethod.POST)
    public CommonResult cancelUserRole(@RequestBody List<String> userNameList) {
        if (ObjectUtils.isEmpty(userNameList)) return CommonResult.failed("请选择要取消授权的用户！");
        //查询条件
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>().in("user_name", userNameList);
        if (sysUserRoleService.remove(queryWrapper)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }


}
