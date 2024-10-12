package com.zhedian.provide.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhedian.provide.system.entity.SysDept;

import java.util.List;


/**
 * 部门表Service接口
 */
public interface SysDeptService extends IService<SysDept> {

    List<SysDept> getDeptList(String deptName, Integer status);
}
