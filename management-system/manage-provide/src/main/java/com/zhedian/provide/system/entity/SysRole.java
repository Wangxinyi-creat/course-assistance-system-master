package com.zhedian.provide.system.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 角色表
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    /**
     * 主键（角色编号)
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限）
     */
    private Integer dataScope;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否显示(0-否   1-是)
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
