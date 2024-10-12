package com.zhedian.provide.scas.entity;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程对象 scas_course
 */
@Data
@TableName("scas_course")
public class ScasCourse implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 教师ID
     */
    private Integer teacherId;
    /**
     * 上课时间
     */
    private String classTime;
    /**
     * 课程类型
     */
    private String courseType;
    /**
     * 课程介绍
     */
    private String courseDescription;
    /**
     * 课程状态
     */
    private Integer status;
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
    private String teacherName;


}
