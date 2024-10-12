package com.zhedian.provide.scas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提问回复对象 scas_question_replies
 */
@Data
@TableName("scas_question_replies")
public class ScasQuestionReplies implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer replyId;
    /**
     * 课程ID
     */
    private Integer courseId;
    /**
     * 提问者的用户名
     */
    private String userName;
    /**
     * 提问内容
     */
    private String questionText;
    /**
     * 回复内容
     */
    private String replyText;
    /**
     * 提问日期
     */
    private Date questionDate;
    /**
     * 回复日期
     */
    private Date replyDate;

    @TableField(exist = false)
    private String courseName;


}
