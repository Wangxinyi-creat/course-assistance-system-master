package com.zhedian.provide.scas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 实验报告表
 *
 * @TableName scas_experiment
 */
@TableName(value = "scas_experiment")
@Data
public class ScasExperiment implements Serializable {
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
     * 作业号
     */
    private String jobNumber;

    /**
     * 学生用户名
     */
    private String userName;

    /**
     * 实验报告标题
     */
    private String experimentTitle;

    /**
     * 实验报告描述
     */
    private String experimentDescription;

    /**
     * 截止日期
     */
    private Date deadline;

    /**
     * 回答
     */
    private String answer;

    /**
     * 上传附件
     */
    private String uploadUrl;

    /**
     * 上传日期
     */
    private Date uploadDate;

    /**
     * 成绩
     */
    private Double grade;

    /**
     * 评审
     */
    private String review;

    /**
     * 正确结果
     */
    private String rightResult;

    /**
     * 实验代码
     */
    private String myCode;

    /**
     * 结果
     */
    private String result;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String courseName;

    @TableField(exist = false)
    private Integer editFlag;
}
