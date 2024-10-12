package com.zhedian.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.provide.system.entity.SysDept;
import com.zhedian.provide.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表 前端控制器
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Resource
    private SysDeptService deptService;

    @ApiOperation("获取全部部门")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult getDeptList(@RequestParam(required = false) String deptName,
                                    @RequestParam(required = false) Integer status) {
        return CommonResult.success(deptService.getDeptList(deptName,status));
    }


    @ApiOperation("删除部门")
    @LogOperation("删除部门")
    @PreAuthorize("@perm.hasPermit('system:dept:delete')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteDept(@PathVariable("id") Integer id) {
        //查询有无下级部门
        List<SysDept> depts = deptService.list(new QueryWrapper<SysDept>().eq("parent_id", id));
        if (ObjectUtils.isNotEmpty(depts)) return CommonResult.failed("该部门下有子级部门，无法删除");
        return CommonResult.success(deptService.removeById(id));
    }

    @ApiOperation("新增部门")
    @LogOperation("新增部门")
    @PreAuthorize("@perm.hasPermit('system:dept:add')")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonResult addDept(@RequestBody SysDept dept) {
        if (deptService.save(dept)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("修改部门")
    @LogOperation("修改部门")
    @PreAuthorize("@perm.hasPermit('system:dept:update')")
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public CommonResult updateDept(@RequestBody SysDept dept) {
        if (deptService.updateById(dept)) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }
}
