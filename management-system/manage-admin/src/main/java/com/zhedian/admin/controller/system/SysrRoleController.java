package com.zhedian.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.system.entity.SysRole;
import com.zhedian.provide.system.entity.SysRoleMenu;
import com.zhedian.provide.system.service.SysRoleMenuService;
import com.zhedian.provide.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 角色表 接口
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/role")
public class SysrRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @ApiOperation("获取角色")
    @PreAuthorize("@perm.hasPermit('system:role:get')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysRole>> getRole(@RequestParam String roleName,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam Integer pageNum) {
        Page<SysRole> rolePage = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(roleName), SysRole::getRoleName, roleName);
        //分页查询
        Page<SysRole> sysRolePage = sysRoleService.page(rolePage,queryWrapper);
        return CommonResult.success(ResultPage.restPage(sysRolePage));
    }

    @ApiOperation("获取角色--供用户选择角色使用")
    @RequestMapping(value = "/getRoleToUser", method = RequestMethod.GET)
    public CommonResult<List<SysRole>> getRoleToUser() {
        //条件构造器
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        //查询角色可用的
        queryWrapper.eq(SysRole::getShowFlag, 1);
        return CommonResult.success(sysRoleService.list(queryWrapper));
    }

    @ApiOperation("添加角色")
    @LogOperation("添加角色")
    @PreAuthorize("@perm.hasPermit('system:role:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addRole(@RequestBody SysRole sysRole) {
        //保存
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            //设置角色菜单，默认首页
//            SysRoleMenu roleMenu = new SysRoleMenu();
//            roleMenu.setRoleId(sysRole.getId());
//            roleMenu.setMenuId(1);
//            sysRoleMenuService.save(roleMenu);
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("修改角色")
    @LogOperation("修改角色")
    @PreAuthorize("@perm.hasPermit('system:role:update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateRole(@RequestBody SysRole sysRole) {
        if (sysRoleService.updateById(sysRole)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除角色")
    @LogOperation("删除角色")
    @PreAuthorize("@perm.hasPermit('system:role:delete')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteRole(@PathVariable("id") Integer id) {
        return CommonResult.success(sysRoleService.removeById(id));
    }

    @ApiOperation("修改角色是否显示")
    @LogOperation("修改角色是否显示")
    @PreAuthorize("@perm.hasPermit('system:role:update')")
    @RequestMapping(value = "/changeStatus/{id}/{showFlag}", method = RequestMethod.POST)
    public CommonResult changeStatus(@PathVariable("id") Integer id,
                                     @PathVariable("showFlag") Boolean showFlag) {
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("show_flag", showFlag)
                .set("update_by", AuthService.getUser().getUserName())
                .set("update_time", new Date())
                .eq("id", id);
        if (sysRoleService.update(updateWrapper)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @Transactional
    @ApiOperation("修改角色菜单权限")
    @LogOperation("修改角色菜单权限")
    @PreAuthorize("@perm.hasPermit('system:role:update')")
    @RequestMapping(value = "/addRoleMenu/{roleId}", method = RequestMethod.POST)
    public CommonResult updateRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        //查询该角色有无菜单权限 若有删除在添加
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        if (CollectionUtils.isNotEmpty(roleMenus)) {
            //删除角色原有菜单权限
            boolean roleMenu = sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
            if (!roleMenu) {
                return CommonResult.failed();
            }
        }
        //添加关系
        for (Integer menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            boolean save = sysRoleMenuService.save(sysRoleMenu);
            if (!save) {
                return CommonResult.failed();
            }
        }
        return CommonResult.success("分配成功");
    }


}
