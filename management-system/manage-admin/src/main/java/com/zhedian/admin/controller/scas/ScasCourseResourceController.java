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
import com.zhedian.provide.scas.entity.ScasCourseResource;
import com.zhedian.provide.scas.entity.ScasMyCourses;
import com.zhedian.provide.scas.service.IScasCourseResourceService;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.system.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程资源Controller
 */
@Api(tags = "课程资源管理")
@RestController
@RequestMapping("/scas/resource")
public class ScasCourseResourceController {

    @Resource
    private IScasCourseResourceService scasCourseResourceService;

    @Resource
    private IScasCourseService scasCourseService;


    @ApiOperation("获取课程资源")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasCourseResource>> getScasCourseResource(@RequestParam Integer pageSize,
                                                                              @RequestParam Integer pageNum,
                                                                              @RequestParam(required = false) String courseId,
                                                                              @RequestParam(required = false) String resourceName) {
        Page<ScasCourseResource> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasCourseResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotNull(courseId), ScasCourseResource::getCourseId, courseId)
                .like(ObjectUtils.isNotNull(resourceName), ScasCourseResource::getResourceName, resourceName);
        SysUser user = AuthService.getUser();
        //判断用户身份 老师只能查看自己的课程资源
        if (user.getIdentity().equals("老师")) {
            List<ScasCourse> courses = scasCourseService.list(new QueryWrapper<ScasCourse>().eq("teacher_id", user.getId()));
            List<Integer> collect = courses.stream().map(ScasCourse::getId).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(collect)) {
                queryWrapper.in(ScasCourseResource::getCourseId, collect);
            } else {
                queryWrapper.eq(ScasCourseResource::getId, 0);
            }
        }
        //分页查询
        Page<ScasCourseResource> ScasCourseResourcePage = scasCourseResourceService.page(page, queryWrapper);
        if (ObjectUtils.isNotEmpty(ScasCourseResourcePage.getRecords())) {
            for (ScasCourseResource record : ScasCourseResourcePage.getRecords()) {
                ScasCourse course = scasCourseService.getById(record.getCourseId());
                if (ObjectUtils.isNotNull(course)) record.setCourseName(course.getCourseName());
            }
        }
        return CommonResult.success(ResultPage.restPage(ScasCourseResourcePage));
    }


    @ApiOperation("新增课程资源")
    @LogOperation("新增课程资源")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasCourseResource(@RequestBody ScasCourseResource scasCourseResource) {
        //保存
        scasCourseResource.setUploadDate(new Date());
        if (scasCourseResourceService.save(scasCourseResource)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }


    @ApiOperation("修改课程资源")
    @LogOperation("修改课程资源")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasCourseResource(@RequestBody ScasCourseResource scasCourseResource) {
        //修改
        if (scasCourseResourceService.updateById(scasCourseResource)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除课程资源")
    @LogOperation("删除课程资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteScasCourseResource(@PathVariable("id") Integer id) {
        return CommonResult.success(scasCourseResourceService.removeById(id));
    }
}
