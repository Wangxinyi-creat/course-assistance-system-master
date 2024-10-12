package com.zhedian.admin.controller.system;

import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.provide.system.entity.SysMenu;
import com.zhedian.provide.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 菜单表 接口
 */

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;


    @ApiOperation("获取全部菜单")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult getMenuList(@RequestParam(required = false) String menuName) {
        return CommonResult.success(sysMenuService.getMenuListTree(menuName));
    }

    @ApiOperation("获取菜单--供角色分配菜单时使用")
    @RequestMapping(value = "/getMenuToRole", method = RequestMethod.GET)
    public CommonResult getMenuToRole() {
        return CommonResult.success(sysMenuService.getMenuToRoleTree());
    }

    @ApiOperation("获取当前登录用户路由")
    @RequestMapping(value = "/getRouterMenu", method = RequestMethod.GET)
    public CommonResult getMenuListByUser() {
        return CommonResult.success(sysMenuService.getRouterMenuList());
    }

    @ApiOperation("获取当前登录用户菜单")
    @RequestMapping(value = "/getUserMenu", method = RequestMethod.GET)
    public CommonResult getUserMenu() {
        return CommonResult.success(sysMenuService.getUserMenu());
    }

    @ApiOperation("获取当前角色菜单")
    @RequestMapping(value = "/getByRoleId", method = RequestMethod.GET)
    public CommonResult getMenuListByRoleId(@RequestParam Integer id) {
        return CommonResult.success(sysMenuService.getMenuListByRoleId(id));
    }

    @ApiOperation("添加菜单")
    @LogOperation("添加菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addMenu(@RequestBody SysMenu sysMenu) {
        return CommonResult.success(sysMenuService.save(sysMenu));
    }

    @ApiOperation("修改菜单")
    @LogOperation("修改菜单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateMenu(@RequestBody SysMenu sysMenu) {
        return CommonResult.success(sysMenuService.updateById(sysMenu));
    }

    @ApiOperation("删除菜单")
    @LogOperation("删除菜单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteMenu(@PathVariable("id") Integer id) {
        return CommonResult.success(sysMenuService.removeById(id));
    }


}
