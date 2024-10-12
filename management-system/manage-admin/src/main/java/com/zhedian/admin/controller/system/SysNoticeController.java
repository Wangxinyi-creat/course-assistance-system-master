package com.zhedian.admin.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.system.entity.SysNotice;
import com.zhedian.provide.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 通知公告Controller
 */
@Api(tags = "通知公告管理")
@RestController
@RequestMapping("/notice")
public class SysNoticeController {

    @Resource
    private ISysNoticeService sysNoticeService;


    @ApiOperation("获取通知公告")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysNotice>> getSysNotice(@RequestParam Integer pageSize,
                                                            @RequestParam Integer pageNum,
                                                            @RequestParam(required = false) Integer status,
                                                            @RequestParam(required = false) String noticeTitle) {
        Page<SysNotice> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotNull(status), SysNotice::getStatus, status)
                .like(ObjectUtils.isNotNull(noticeTitle), SysNotice::getNoticeTitle, noticeTitle);
        //分页查询
        Page<SysNotice> SysNoticePage = sysNoticeService.page(page, queryWrapper);
        return CommonResult.success(ResultPage.restPage(SysNoticePage));
    }

    @ApiOperation("通过id获取通知公告")
    @RequestMapping(value = "/get/{noticeId}", method = RequestMethod.GET)
    public CommonResult getSysNotice(@PathVariable("noticeId") Long noticeId) {
        return CommonResult.success(sysNoticeService.getById(noticeId));
    }


    @ApiOperation("新增通知公告")
    @LogOperation("新增通知公告")
    @PreAuthorize("@perm.hasPermit('system:notice:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addSysNotice(@RequestBody SysNotice sysNotice) {
        //保存
        if (sysNoticeService.save(sysNotice)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }


    @ApiOperation("修改通知公告")
    @LogOperation("修改通知公告")
    @PreAuthorize("@perm.hasPermit('system:notice:update')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateSysNotice(@RequestBody SysNotice sysNotice) {
        //修改
        if (sysNoticeService.updateById(sysNotice)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("修改公告状态")
    @LogOperation("修改公告状态")
    @PreAuthorize("@perm.hasPermit('system:notice:update')")
    @RequestMapping(value = "/changeStatus/{noticeId}/{status}", method = RequestMethod.POST)
    public CommonResult changeStatus(@PathVariable("noticeId") Long noticeId,
                                     @PathVariable("status") Boolean status) {
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<SysNotice>();
        updateWrapper.set("status", status).eq("notice_id", noticeId);
        if (sysNoticeService.update(updateWrapper)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除通知公告")
    @LogOperation("删除通知公告")
    @PreAuthorize("@perm.hasPermit('system:notice:delete')")
    @RequestMapping(value = "/delete/{noticeId}", method = RequestMethod.DELETE)
    public CommonResult deleteSysNotice(@PathVariable("noticeId") Long noticeId) {
        return CommonResult.success(sysNoticeService.removeById(noticeId));
    }
}
