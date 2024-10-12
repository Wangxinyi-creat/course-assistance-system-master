package com.zhedian.provide.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhedian.provide.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 菜单表 Mapper 接口
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    /**
     * 通过登录账号查询菜单
     * TRIM(m.id) != '' 防止查出来有一条空数据
     *
     * @param userName 账号名称
     * @return
     */
    List<SysMenu> getMenuListTreeByUser(@Param("userName") String userName, @Param("menuType") Integer menuType);


    /**
     * 通过登录账号查询权限集合
     *
     * @param userName 账号名称
     * @return
     */
    @Select("SELECT DISTINCT m.permission " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.user_name=ur.user_name " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id =rm.role_id " +
            "LEFT JOIN sys_menu m ON rm.menu_id=m.id " +
            "WHERE u.user_name=#{userName}")
    List<String> getPermissionIdByUser(String userName);


    /**
     * 通过角色id查询菜单
     *
     * @param id 角色id
     * @return
     */
    @Select("SELECT m.* FROM sys_menu m RIGHT JOIN sys_role_menu rm ON m.id=rm.menu_id WHERE rm.role_id=#{id}")
    List<SysMenu> getMenuListByRoleId(@Param("id") Integer id);

    /**
     * 通过用户名查询角色数据范围
     *
     * @param userName 账号名称
     * @return
     */
    @Select("SELECT r.data_scope " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.user_name=ur.user_name " +
            "LEFT JOIN sys_role r ON ur.role_id =r.id " +
            "WHERE u.user_name=#{userName}")
    List<Integer> getRoleDataScopeByUser(@Param("userName") String userName);
}
