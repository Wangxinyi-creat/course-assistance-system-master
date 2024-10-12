package com.zhedian.admin.controller.scas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.scas.entity.ScasCourse;
import com.zhedian.provide.scas.entity.ScasCourseReviews;
import com.zhedian.provide.scas.entity.ScasQuestionReplies;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.scas.service.IScasQuestionRepliesService;
import com.zhedian.provide.system.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提问回复Controller
 */
@Api(tags = "提问回复管理")
@RestController
@RequestMapping("/scas/replies")
public class ScasQuestionRepliesController {

    @Resource
    private IScasQuestionRepliesService scasQuestionRepliesService;

    @Resource
    private IScasCourseService scasCourseService;


    @ApiOperation("获取提问回复")
    @PreAuthorize("@perm.hasPermit('scas:replies:get')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasQuestionReplies>> getScasQuestionReplies(@RequestParam Integer pageSize,
                                                                                @RequestParam Integer pageNum) {
        Page<ScasQuestionReplies> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasQuestionReplies> queryWrapper = new LambdaQueryWrapper<>();
        //分页查询
        Page<ScasQuestionReplies> ScasQuestionRepliesPage = scasQuestionRepliesService.page(page, queryWrapper);
        //查询课程名称
        if (ObjectUtils.isNotEmpty(ScasQuestionRepliesPage.getRecords())) {
            for (ScasQuestionReplies record : ScasQuestionRepliesPage.getRecords()) {
                ScasCourse course = scasCourseService.getById(record.getCourseId());
                if (ObjectUtils.isNotNull(course)) record.setCourseName(course.getCourseName());
            }
        }
        return CommonResult.success(ResultPage.restPage(ScasQuestionRepliesPage));
    }


    @ApiOperation("新增提问回复")
    @LogOperation("新增提问回复")
    @PreAuthorize("@perm.hasPermit('scas:replies:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasQuestionReplies(@RequestBody ScasQuestionReplies scasQuestionReplies) {
        //保存
        SysUser user = AuthService.getUser();
        scasQuestionReplies.setUserName(user.getUserName());
        scasQuestionReplies.setQuestionDate(new Date());
        if (scasQuestionRepliesService.save(scasQuestionReplies)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }


    @ApiOperation("修改提问")
    @LogOperation("修改提问")
    @PreAuthorize("@perm.hasPermit('scas:replies:update')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasQuestionReplies(@RequestBody ScasQuestionReplies scasQuestionReplies) {
        //修改
        if (scasQuestionRepliesService.updateById(scasQuestionReplies)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("提问回复")
    @LogOperation("提问回复")
    @PreAuthorize("@perm.hasPermit('scas:replies:reply')")
    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public CommonResult replyScasQuestionReplies(@RequestBody ScasQuestionReplies scasQuestionReplies) {
        //修改
        scasQuestionReplies.setReplyDate(new Date());
        if (scasQuestionRepliesService.updateById(scasQuestionReplies)) {
            return CommonResult.success("回复成功");
        }
        return CommonResult.failed("回复失败");
    }

    @ApiOperation("删除提问回复")
    @LogOperation("删除提问回复")
    @PreAuthorize("@perm.hasPermit('scas:replies:delete')")
    @RequestMapping(value = "/delete/{replyId}", method = RequestMethod.DELETE)
    public CommonResult deleteScasQuestionReplies(@PathVariable("replyId") Integer replyId) {
        return CommonResult.success(scasQuestionRepliesService.removeById(replyId));
    }
}
