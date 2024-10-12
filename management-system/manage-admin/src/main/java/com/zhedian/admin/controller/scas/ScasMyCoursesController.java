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
import com.zhedian.provide.scas.entity.ScasExperiment;
import com.zhedian.provide.scas.entity.ScasMyCourses;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.scas.service.IScasMyCoursesService;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 我的课程Controller
 */
@Api(tags = "我的课程管理")
@RestController
@RequestMapping("/scas/courses")
public class ScasMyCoursesController {

    @Resource
    private IScasMyCoursesService scasMyCoursesService;

    @Resource
    private IScasCourseService scasCourseService;

    @Resource
    private SysUserService userService;


    @ApiOperation("获取我的课程")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasMyCourses>> getScasMyCourses(@RequestParam Integer pageSize,
                                                                    @RequestParam Integer pageNum,
                                                                    @RequestParam(required = false) Integer courseId,
                                                                    @RequestParam(required = false) String userName,
                                                                    @RequestParam(required = false) String major) {
        Page<ScasMyCourses> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasMyCourses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(courseId), ScasMyCourses::getCourseId, courseId)
                .like(ObjectUtils.isNotNull(userName), ScasMyCourses::getUserName, userName);
        if (ObjectUtils.isNotNull(major)) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(SysUser::getMajor, major);
            List<SysUser> sysUsers = userService.list(wrapper);
            List<String> collect = sysUsers.stream().map(SysUser::getUserName).collect(Collectors.toList());
            if (ObjectUtils.isNotNull(collect)) {
                queryWrapper.in(ScasMyCourses::getUserName, collect);
            }
        }
        SysUser user = AuthService.getUser();
        //判断用户身份 查询学生的课程 或者老师的课程
        if (user.getIdentity().equals("学生")) {
            queryWrapper.eq(ScasMyCourses::getUserName, user.getUserName());
        } else if (user.getIdentity().equals("老师")) {
            List<ScasCourse> courses = scasCourseService.list(new QueryWrapper<ScasCourse>().eq("teacher_id", user.getId()));
            List<Integer> collect = courses.stream().map(ScasCourse::getId).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(collect)) {
                queryWrapper.in(ScasMyCourses::getCourseId, collect);
            } else {
                queryWrapper.eq(ScasMyCourses::getId, 0);
            }
        }
        //分页查询
        Page<ScasMyCourses> ScasMyCoursesPage = scasMyCoursesService.page(page, queryWrapper);
        //查询课程名称
        if (ObjectUtils.isNotEmpty(ScasMyCoursesPage.getRecords())) {
            for (ScasMyCourses record : ScasMyCoursesPage.getRecords()) {
                ScasCourse course = scasCourseService.getById(record.getCourseId());
                if (ObjectUtils.isNotNull(course)) {
                    record.setCourseName(course.getCourseName());
                    record.setCourseType(course.getCourseType());
                    record.setCourseStatus(course.getStatus());
                    SysUser sysUser = userService.getById(course.getTeacherId());
                    record.setTeacherName(sysUser.getNickName());
                }
            }
        }
        return CommonResult.success(ResultPage.restPage(ScasMyCoursesPage));
    }


    @ApiOperation("新增我的课程")
    @LogOperation("新增我的课程")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasMyCourses(@RequestBody ScasMyCourses scasMyCourses) {
        SysUser user = AuthService.getUser();
        //查询课程是否存在
        LambdaQueryWrapper<ScasMyCourses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScasMyCourses::getCourseId, scasMyCourses.getCourseId())
                .eq(ScasMyCourses::getUserName, user.getUserName());
        ScasMyCourses myCourses = scasMyCoursesService.getOne(queryWrapper);
        if (ObjectUtils.isNotNull(myCourses)) {
            return CommonResult.failed("课程已报名");
        }
        //保存
        scasMyCourses.setUserName(user.getUserName());
        scasMyCourses.setEnrollmentDate(new Date());
        if (scasMyCoursesService.save(scasMyCourses)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }


    @ApiOperation("修改我的课程")
    @LogOperation("修改我的课程")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasMyCourses(@RequestBody ScasMyCourses scasMyCourses) {
        //修改
        if (scasMyCoursesService.updateById(scasMyCourses)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除我的课程")
    @LogOperation("删除我的课程")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteScasMyCourses(@PathVariable("id") Integer id) {
        return CommonResult.success(scasMyCoursesService.removeById(id));
    }
}
