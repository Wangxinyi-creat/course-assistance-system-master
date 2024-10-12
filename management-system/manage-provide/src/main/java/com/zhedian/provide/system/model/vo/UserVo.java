package com.zhedian.provide.system.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户表显示vo
 */
@Data
public class UserVo {

    private Integer id;

    private String userName;

    private String password;

    private String nickName;

    private String phone;

    private String email;

    private String headSrc;

    private Integer sex;

    private Boolean status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String roleName;

    private Integer deptId;

    private String deptName;

    private String identity;

    private String major;

    private String className;

    private List<Integer> roleId;

}
