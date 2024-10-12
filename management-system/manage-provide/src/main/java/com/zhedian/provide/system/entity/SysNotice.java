package com.zhedian.provide.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告对象 sys_notice
 */
@Data
@TableName("sys_notice")
public class SysNotice implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    private Long noticeId;
    /**
     * 公告标题
     */
    private String noticeTitle;
    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;
    /**
     * 公告内容
     */
    private String noticeContent;
    /**
     * 公告状态（0正常 1关闭）
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
    /**
     * 备注
     */
    private String remark;


}
