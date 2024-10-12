package com.zhedian.provide.scas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的课程对象 scas_my_courses
 */
@Data
@TableName("scas_my_courses")
public class ScasMyCourses implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID，关联用户表的user_name字段
     */
    private String userName;
    /**
     * 课程ID，关联课程表的id字段
     */
    private Integer courseId;
    /**
     * 选课日期
     */
    private Date enrollmentDate;
    /**
     * 平时成绩
     */
    private Double ordinaryGrade;
    /**
     * 考试成绩
     */
    private Double examGrade;
    /**
     * 总分
     */
    private Double grade;

    @TableField(exist = false)
    private String courseName;
    @TableField(exist = false)
    private String courseType;
    @TableField(exist = false)
    private Integer courseStatus;
    @TableField(exist = false)
    private String teacherName;

}
