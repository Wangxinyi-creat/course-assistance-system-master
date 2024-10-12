package com.zhedian.provide.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

;

/**
 * 账户角色表
 */
@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 角色编号
     */
    private Integer roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
