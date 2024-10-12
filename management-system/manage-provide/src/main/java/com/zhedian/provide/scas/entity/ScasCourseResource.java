package com.zhedian.provide.scas.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程资源对象 scas_course_resource
 */
@Data
@TableName("scas_course_resource")
public class ScasCourseResource implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 课程ID
     */
    private Integer courseId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源描述
     */
    private String resourceDescription;
    /**
     * 资源链接
     */
    private String resourceUrl;
    /**
     * 上传日期
     */
    private Date uploadDate;
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
    private String courseName;


}
