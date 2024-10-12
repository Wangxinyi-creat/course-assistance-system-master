package com.zhedian.admin.controller.scas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.scas.entity.ScasCourse;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 课程Controller
 */
@Api(tags = "课程管理")
@RestController
@RequestMapping("/scas/course")
public class ScasCourseController {

    @Resource
    private IScasCourseService scasCourseService;

    @Resource
    private SysUserService userService;


    @ApiOperation("获取课程")
    @PreAuthorize("@perm.hasPermit('scas:course:get')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasCourse>> getScasCourse(@RequestParam Integer pageSize,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam(required = false) String courseName,
                                                              @RequestParam(required = false) String status) {
        Page<ScasCourse> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(courseName), ScasCourse::getCourseName, courseName)
                .eq(ObjectUtils.isNotNull(status), ScasCourse::getStatus, status);
        SysUser loginUser = AuthService.getUser();
        // 如果是老师，只能查看自己发布的课程
        if (loginUser.getIdentity().equals("老师")) {
            queryWrapper.eq(ScasCourse::getTeacherId, loginUser.getId());
        }
        //分页查询
        Page<ScasCourse> coursePage = scasCourseService.page(page, queryWrapper);
        if (ObjectUtils.isNotEmpty(coursePage.getRecords())) {
            for (ScasCourse record : coursePage.getRecords()) {
                SysUser user = userService.getById(record.getTeacherId());
                record.setTeacherName(user.getNickName());
            }
        }
        return CommonResult.success(ResultPage.restPage(coursePage));
    }

    @ApiOperation("获取课程详情")
    @GetMapping("getDetail")
    public CommonResult getDetail(@RequestParam Integer id) {
        ScasCourse course = scasCourseService.getById(id);
        if (ObjectUtils.isNotNull(course)) {
            SysUser user = userService.getById(course.getTeacherId());
            course.setTeacherName(user.getNickName());
        }
        return CommonResult.success(course);
    }

    @ApiOperation("获取创建课程")
    @RequestMapping(value = "/getCreate", method = RequestMethod.GET)
    public CommonResult getCreateScasCourse(@RequestParam Integer status) {
        SysUser user = AuthService.getUser();
        if (ObjectUtils.isEmpty(user)) {
            return CommonResult.failed("用户未登录");
        }
        if (status.equals(1) || user.getUserName().equals("admin") || user.getUserName().equals("system")) {
            return CommonResult.success(scasCourseService.list(null));
        }

        LambdaQueryWrapper<ScasCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScasCourse::getTeacherId, user.getId());
        return CommonResult.success(scasCourseService.list(queryWrapper));
    }


    @ApiOperation("新增课程")
    @LogOperation("新增课程")
    @PreAuthorize("@perm.hasPermit('scas:course:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasCourse(@RequestBody ScasCourse scasCourse) {
        //保存
        SysUser user = AuthService.getUser();
        scasCourse.setTeacherId(user.getId());
        if (scasCourseService.save(scasCourse)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }


    @ApiOperation("修改课程")
    @LogOperation("修改课程")
    @PreAuthorize("@perm.hasPermit('scas:course:update')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasCourse(@RequestBody ScasCourse scasCourse) {
        //修改
        if (scasCourseService.updateById(scasCourse)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除课程")
    @LogOperation("删除课程")
    @PreAuthorize("@perm.hasPermit('scas:course:delete')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteScasCourse(@PathVariable("id") Integer id) {
        return CommonResult.success(scasCourseService.removeById(id));
    }
}
