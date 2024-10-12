package com.zhedian.provide.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 部门表
 *
 * @TableName sys_dept
 */
@TableName(value ="sys_dept")
@Data
public class SysDept implements Serializable {
    /**
     * 部门id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 父部门id
     */
    private Integer parentId;

    /**
     * 显示顺序
     */
    private Integer sortNum;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态
     */
    private Boolean status;

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
    private List<SysDept> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
