package com.zhedian.provide.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhedian.provide.system.entity.SysMenu;
import com.zhedian.provide.system.model.vo.menu.MenuVo;
import com.zhedian.provide.system.model.vo.menu.RouterMenuVo;

import java.util.List;

/**
 * 菜单表 服务类
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 通过账号名称查询权限集合
     *
     * @param userName 账号名称
     * @return
     */
    List<String> getPermissionIdByUser(String userName);

    /**
     * 递归获取菜单数据
     *
     * @return
     */
    List<MenuVo> getMenuListTree(String menuName);


    /**
     * 获取菜单--供角色分配菜单时使用
     *
     * @return
     */
    List<MenuVo> getMenuToRoleTree();

    /**
     * 获取当前登录用户的路由
     *
     * @return
     */
    List<RouterMenuVo> getRouterMenuList();


    /**
     * 获取当前登录用户菜单数据
     *
     * @return
     */
    List<MenuVo> getUserMenu();

    /**
     * 根据角色id获取菜单权限
     *
     * @param id 角色id
     * @return
     */
    List<SysMenu> getMenuListByRoleId(Integer id);
}
