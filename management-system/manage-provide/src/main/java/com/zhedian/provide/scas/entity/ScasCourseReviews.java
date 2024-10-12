package com.zhedian.provide.scas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程评价对象 scas_course_reviews
 */
@Data
@TableName("scas_course_reviews")
public class ScasCourseReviews implements Serializable {

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
     * 用户ID
     */
    private String userName;
    /**
     * 评价内容
     */
    private String reviewText;
    /**
     * 评价日期
     */
    private Date reviewDate;
    /**
     * 评分，范围1到5
     */
    private Integer rating;

    @TableField(exist = false)
    private String courseName;


}
