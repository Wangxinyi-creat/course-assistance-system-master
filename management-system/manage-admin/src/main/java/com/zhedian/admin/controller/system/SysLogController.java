package com.zhedian.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.log.entity.SysLoginLog;
import com.zhedian.provide.log.entity.SysOperateLog;
import com.zhedian.provide.log.service.ISysLoginLogService;
import com.zhedian.provide.log.service.ISysOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 登录日志Controller
 */
@Api(tags = "登录日志管理")
@RestController
@RequestMapping("/log")
public class SysLogController {

    @Resource
    private ISysLoginLogService sysLoginLogService;

    @Resource
    private ISysOperateLogService sysOperateLogService;


    @ApiOperation("获取登录日志")
    @RequestMapping(value = "/login/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysLoginLog>> getLoginLog(@RequestParam String userName,
                                                             @RequestParam Integer pageSize,
                                                             @RequestParam Integer pageNum) {
        Page<SysLoginLog> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(userName), SysLoginLog::getUserName, userName)
                .orderByDesc(SysLoginLog::getLoginTime);
        //分页查询
        Page<SysLoginLog> SysLoginLogPage = sysLoginLogService.page(page, queryWrapper);
        return CommonResult.success(ResultPage.restPage(SysLoginLogPage));
    }


    @ApiOperation("获取系统操作日志")
    @RequestMapping(value = "/operate/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysOperateLog>> getOperate(@RequestParam String userName,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam Integer pageNum) {
        Page<SysOperateLog> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<SysOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(userName), SysOperateLog::getUserName, userName)
                .orderByDesc(SysOperateLog::getCreateTime);
        //分页查询
        Page<SysOperateLog> SysOperateLogPage = sysOperateLogService.page(page, queryWrapper);
        return CommonResult.success(ResultPage.restPage(SysOperateLogPage));
    }
}
