package com.zhedian.admin.controller.scas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.scas.entity.ScasCourse;
import com.zhedian.provide.scas.entity.ScasCourseReviews;
import com.zhedian.provide.scas.entity.ScasMyCourses;
import com.zhedian.provide.scas.service.IScasCourseReviewsService;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.system.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程评价Controller
 */
@Api(tags = "课程评价管理")
@RestController
@RequestMapping("/scas/reviews")
public class ScasCourseReviewsController {

    @Resource
    private IScasCourseReviewsService scasCourseReviewsService;

    @Resource
    private IScasCourseService scasCourseService;


    @ApiOperation("获取课程评价")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasCourseReviews>> getScasCourseReviews(@RequestParam Integer pageSize,
                                                                            @RequestParam Integer pageNum,
                                                                            @RequestParam(required = false) Integer courseId,
                                                                            @RequestParam(required = false) String userName) {
        Page<ScasCourseReviews> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasCourseReviews> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(courseId), ScasCourseReviews::getCourseId, courseId)
                .like(ObjectUtils.isNotNull(userName), ScasCourseReviews::getUserName, userName);
        SysUser user = AuthService.getUser();
        //判断用户身份 查询学生的课程 或者老师的课程
        if (user.getIdentity().equals("学生")) {
            queryWrapper.eq(ScasCourseReviews::getUserName, user.getUserName());
        } else if (user.getIdentity().equals("老师")) {
            List<ScasCourse> courses = scasCourseService.list(new QueryWrapper<ScasCourse>().eq("teacher_id", user.getId()));
            List<Integer> collect = courses.stream().map(ScasCourse::getId).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(collect)) {
                queryWrapper.in(ScasCourseReviews::getCourseId, collect);
            } else {
                queryWrapper.eq(ScasCourseReviews::getId, 0);
            }
        }
        //分页查询
        Page<ScasCourseReviews> ScasCourseReviewsPage = scasCourseReviewsService.page(page, queryWrapper);
        //查询课程名称
        if (ObjectUtils.isNotEmpty(ScasCourseReviewsPage.getRecords())) {
            for (ScasCourseReviews record : ScasCourseReviewsPage.getRecords()) {
                ScasCourse course = scasCourseService.getById(record.getCourseId());
                if (ObjectUtils.isNotNull(course)) record.setCourseName(course.getCourseName());
            }
        }
        return CommonResult.success(ResultPage.restPage(ScasCourseReviewsPage));
    }


    @ApiOperation("新增课程评价")
    @LogOperation("新增课程评价")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasCourseReviews(@RequestBody ScasCourseReviews scasCourseReviews) {
        //保存
        SysUser user = AuthService.getUser();
        scasCourseReviews.setUserName(user.getUserName());
        scasCourseReviews.setReviewDate(new Date());
        if (scasCourseReviewsService.save(scasCourseReviews)) {
            return CommonResult.success("评价成功");
        }
        return CommonResult.failed("评价失败");
    }


    @ApiOperation("修改课程评价")
    @LogOperation("修改课程评价")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasCourseReviews(@RequestBody ScasCourseReviews scasCourseReviews) {
        //修改
        if (scasCourseReviewsService.updateById(scasCourseReviews)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除课程评价")
    @LogOperation("删除课程评价")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteScasCourseReviews(@PathVariable("id") Integer id) {
        return CommonResult.success(scasCourseReviewsService.removeById(id));
    }
}
