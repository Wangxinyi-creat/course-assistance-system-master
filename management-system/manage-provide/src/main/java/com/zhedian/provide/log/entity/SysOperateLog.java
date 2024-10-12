package com.zhedian.provide.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志对象 sys_operate_log
 */
@Data
@TableName("sys_operate_log")
public class SysOperateLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;    

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 请求方法名称
     */
    private String methodName;
    /**
     * 操作人
     */
    private String userName;
    /**
     * 请求IP
     */
    private String reqIp;
    /**
     * 请求接口
     */
    private String reqInterface;
    /**
     * 请求方式
     */
    private String reqType;
    /**
     * 状态（0-失败  1-正常   2-异常）
     */
    private Integer status;
    /**
     * 请求参数
     */
    private String reqParam;
    /**
     * 返回参数
     */
    private String jsonResult;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 系统类型
     */
    private String os;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 执行时长(毫秒)
     */
    private String expendTime;


}
