package com.zhedian.provide.system.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.system.entity.SysMenu;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.mapper.SysMenuMapper;
import com.zhedian.provide.system.model.vo.menu.MenuVo;
import com.zhedian.provide.system.model.vo.menu.Meta;
import com.zhedian.provide.system.model.vo.menu.RouterMenuVo;
import com.zhedian.provide.system.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单表 服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final static String admin = "admin";

    private final static Integer one = 1;
    private final static Integer two = 2;

    /**
     * 通过账号名称查询权限集合
     *
     * @param userName 账号名称
     * @return
     */
    @Override
    public List<String> getPermissionIdByUser(String userName) {
        List<String> permissionIdS;
        //admin 查询全部
        if (admin.equals(userName)) {
            List<SysMenu> sysMenuList = list(new QueryWrapper<SysMenu>().isNotNull("permission"));
            permissionIdS = sysMenuList.stream().map(SysMenu::getPermission).collect(Collectors.toList());
        } else {
            //查询用户数据权限   1全部数据  2自定义数据
            List<Integer> dataScopeByUser = getBaseMapper().getRoleDataScopeByUser(userName);
            if (dataScopeByUser.contains(one)) {
                //全部数据权限,从可见菜单中 获取权限
                QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("show_flag", one).isNotNull("permission");
                permissionIdS = list(queryWrapper).stream().map(SysMenu::getPermission).collect(Collectors.toList());
            } else {
                //自定义数据权限,连表查询数据权限
                permissionIdS = getBaseMapper().getPermissionIdByUser(userName);
            }
        }
        return permissionIdS;
    }

    /**
     * 递归获取菜单数据(菜单管理查询)
     *
     * @return
     */
    @Override
    public List<MenuVo> getMenuListTree(String menuName) {
        //查询所有菜单数据
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(menuName), SysMenu::getMenuName, menuName)
                .orderByAsc(SysMenu::getSortId);
        List<SysMenu> sysMenuList = list(queryWrapper);
        //todo：查询下级?

        //生成树形结构数据返回
        return BuildTreeList(sysMenuList);
    }

    /**
     * 获取菜单--供角色分配菜单时使用
     *
     * @return
     */
    @Override
    public List<MenuVo> getMenuToRoleTree() {
        //条件构造器
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("show_flag", one).orderByAsc("sort_id");
        //生成树形结构数据返回
        return BuildTreeList(list(queryWrapper));
    }

    /**
     * 获取当前登录用户的路由
     *
     * @return
     */
    @Override
    public List<RouterMenuVo> getRouterMenuList() {
        //获取当前登录用户
        SysUser user = AuthService.getUser();

        //查询当前用户路由   如果是admin-查全部
        List<SysMenu> sysMenuList;
        if (admin.equals(user.getUserName())) {
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().eq("menu_type", one);
            sysMenuList = list(queryWrapper);
        } else {
            //查询用户数据权限   1全部数据  2自定义数据
            List<Integer> dataScopeByUser = getBaseMapper().getRoleDataScopeByUser(user.getUserName());
            if (dataScopeByUser.contains(one)) {
                //全部数据权限,从可见菜单中 获取权限
                QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("show_flag", one).eq("menu_type", one);
                sysMenuList = list(queryWrapper);
            } else {
                //自定义数据权限,连表查询
                sysMenuList = getBaseMapper().getMenuListTreeByUser(user.getUserName(), one);
            }

        }

        //数据转换成router
        ArrayList<RouterMenuVo> routers = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            RouterMenuVo routerMenuVo = new RouterMenuVo();
            BeanUtils.copyProperties(sysMenu, routerMenuVo);
            //设置meta信息
            routerMenuVo.setMeta(new Meta(sysMenu.getMenuName(), sysMenu.getIsLink(), sysMenu.getIsIframe(),
                    sysMenu.getIsHide(), sysMenu.getIsAffix(), sysMenu.getElIcon()));
            routers.add(routerMenuVo);
        }

        return routers;
    }

    /**
     * 获取当前登录用户菜单数据
     *
     * @return
     */
    @Override
    public List<MenuVo> getUserMenu() {
        //获取当前登录用户
        SysUser user = AuthService.getUser();
        //查询当前用户路由   如果是admin-查全部
        List<SysMenu> sysMenuList;
        if (admin.equals(user.getUserName())) {
            sysMenuList = list(new QueryWrapper<SysMenu>().ne("menu_type", two).eq("is_hide", 0).orderByAsc("sort_id"));
        } else {
            List<SysMenu> menuList = new ArrayList<>();
            //查询用户数据权限   1全部数据  2自定义数据
            List<Integer> dataScopeByUser = getBaseMapper().getRoleDataScopeByUser(user.getUserName());
            if (dataScopeByUser.contains(one)) {
                //全部数据权限,获取所有可见菜单中
                QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("show_flag", one).eq("is_hide", 0).orderByAsc("sort_id");
                //查询
                menuList = list(queryWrapper);
            } else {
                //自定义数据权限,连表查询
                List<SysMenu> listTreeByUser = getBaseMapper().getMenuListTreeByUser(user.getUserName(), null);
                menuList = listTreeByUser.stream().filter(item -> !item.getIsHide()).collect(Collectors.toList());
            }
            //筛选  除按钮外的菜单
            sysMenuList = menuList.stream().filter(item -> item.getMenuType() != two).collect(Collectors.toList());
        }
        return BuildTreeList(sysMenuList);
    }

    /**
     * 根据角色id获取菜单权限
     *
     * @param id 角色id
     * @return
     */
    @Override
    public List<SysMenu> getMenuListByRoleId(Integer id) {
        //根据角色id查询数据
        List<SysMenu> sysMenuList = getBaseMapper().getMenuListByRoleId(id);
        //生成树形结构数据返回
        return sysMenuList;
    }


    /**
     * 菜单树生成调用方法
     *
     * @param sysMenuList 所有菜单集合
     * @return
     */
    private List<MenuVo> BuildTreeList(List<SysMenu> sysMenuList) {
        //构造Vo返回
        ArrayList<MenuVo> MenusList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sysMenuList)) return MenusList;

        //数据复制
        for (SysMenu sysMenu : sysMenuList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(sysMenu, menuVo);
            MenusList.add(menuVo);
        }

        //获取该菜单下的一级目录
        List<MenuVo> parentList = MenusList.stream().filter(item -> item.getParentId().equals(0)).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(parentList)) return MenusList;

        //遍历所有主菜单,分别获取所有主菜单的所有子菜单
        for (MenuVo treeMenu : parentList) {
            //获取所有子菜单 递归
            List<MenuVo> childrenList = getChildrenMenu(treeMenu.getId(), MenusList);
            //实体类下的子菜单集合
            if (CollectionUtils.isNotEmpty(childrenList)) {
                treeMenu.setChildren(childrenList);
            }
        }
        return parentList;
    }

    /**
     * 递归获取子菜单
     *
     * @param id        父级id
     * @param MenusList 所有菜单集合
     * @return
     */
    private List<MenuVo> getChildrenMenu(Integer id, List<MenuVo> MenusList) {
        //在所有菜单列表中  获取子菜单
        List<MenuVo> childrenList = MenusList.stream().filter(item -> id.equals(item.getParentId())).collect(Collectors.toList());

        //递归，遍历所有的子菜单
        if (CollectionUtils.isNotEmpty(childrenList)) {
            for (MenuVo info : childrenList) {
                List<MenuVo> sysMenus = getChildrenMenu(info.getId(), MenusList);
                if (CollectionUtils.isNotEmpty(sysMenus)) {
                    info.setChildren(sysMenus);
                }
            }
        }
        //如果子菜单为空的话，那就说明某菜单下没有子菜单了，直接返回空,子菜单为空的话就不会继续迭代了
        return childrenList;
    }
}
