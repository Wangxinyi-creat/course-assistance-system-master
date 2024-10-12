package com.zhedian.provide.system.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 菜单表
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    /**
     * 菜单编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径(位置)
     */
    private String component;

    /**
     * 菜单图标(element-ui 图标样式)
     */
    private String elIcon;

    /**
     * 外部链接
     */
    private String isLink;

    /**
     * 是否内嵌(0-否   1-是)
     */
    private Boolean isIframe;

    /**
     * 是否隐藏-侧边栏(0-否   1-是)
     */
    private Boolean isHide;

    /**
     * 是否固定tagsView(0-否   1-是)
     */
    private Boolean isAffix;


    /**
     * 菜单类型(0-目录，1-菜单  2-按钮)
     */
    private Integer menuType;

    /**
     * 指向父类编号(0表示为父节点，若为子类填写父类id)
     */
    private Integer parentId;

    /**
     * 按钮权限标识
     */
    private String permission;

    /**
     * 排序
     */
    private Integer sortId;

    /**
     * 是否可见(0-否   1-是)
     */
    private Boolean showFlag;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)//创建与修改时自动填充
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)//创建与修改时自动填充
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
