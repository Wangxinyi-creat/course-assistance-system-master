package com.zhedian.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.system.entity.SysDictData;
import com.zhedian.provide.system.entity.SysDictType;
import com.zhedian.provide.system.model.dto.DictDto;
import com.zhedian.provide.system.service.SysDictDataService;
import com.zhedian.provide.system.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 字典相关表 接口
 */
@Api(tags = "字典管理")
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Resource
    private SysDictTypeService typeService;

    @Resource
    private SysDictDataService dataService;

    @ApiOperation("获取字典数据(类型+数据)")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<SysDictType>> getDict(@RequestParam(required = false) String dictName,
                                                         @RequestParam Integer pageSize,
                                                         @RequestParam Integer pageNum) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(dictName), SysDictType::getDictName, dictName);
        //分页查询
        Page<SysDictType> dictTypePage = typeService.page(page, queryWrapper);
        //查询字典所属类型下的数据
        if (ObjectUtils.isNotNull(dictTypePage.getRecords())) {
            for (SysDictType record : dictTypePage.getRecords()) {
                List<SysDictData> dictDataList = dataService.list(new QueryWrapper<SysDictData>().eq("dict_type", record.getDictType()));
                record.setDictDataList(dictDataList);
            }
        }
        return CommonResult.success(ResultPage.restPage(dictTypePage));
    }

    @ApiOperation("获取字典(通过字典类型获取数据)")
    @RequestMapping(value = "/getByDictType", method = RequestMethod.GET)
    public CommonResult getByDictType(@RequestParam String dictType) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        List<SysDictData> dictDataList = dataService.list(queryWrapper);
        return CommonResult.success(dictDataList);
    }

    @ApiOperation("添加字典数据")
    @LogOperation("添加字典数据")
    @Transactional
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addDict(@RequestBody DictDto dictDto) {
        //获取当前登录用户
        String userName = AuthService.getUser().getUserName();
        //赋值
        SysDictType dictType = dictDto.getDictType();
        dictType.setCreateBy(userName);
        dictType.setCreateTime(new Date());
        dictType.setUpdateBy(userName);
        dictType.setUpdateTime(new Date());
        //保存类型
        typeService.save(dictType);
        //赋值
        List<SysDictData> dataList = dictDto.getDictDataList();
        dataList.stream().forEach(obj -> obj.setDictType(dictType.getDictType()));
        //保存字典数据
        dataService.saveBatch(dataList);

        return CommonResult.success("添加成功");
    }

    @ApiOperation("修改字典数据")
    @LogOperation("修改字典数据")
    @Transactional
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateDict(@RequestBody DictDto dictDto) {
        //获取当前登录用户
        String userName = AuthService.getUser().getUserName();
        //赋值
        SysDictType dictType = dictDto.getDictType();
        dictType.setUpdateBy(userName);
        dictType.setUpdateTime(new Date());
        typeService.updateById(dictType);
        //删除原子数据
        dataService.remove(new QueryWrapper<SysDictData>().eq("dict_type", dictType.getOldDictType()));
        //添加新的数据
        List<SysDictData> dataList = dictDto.getDictDataList();
        dataList.stream().forEach(obj -> obj.setDictType(dictType.getDictType()));
        //保存字典数据
        dataService.saveBatch(dataList);

        return CommonResult.success("修改成功");
    }


    @ApiOperation("删除字典数据")
    @LogOperation("删除字典数据")
    @Transactional
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult deleteDict(@RequestBody SysDictType dictType) {
        //删除类型
        boolean a = typeService.removeById(dictType.getDictId());
        boolean b = dataService.remove(new QueryWrapper<SysDictData>().eq("dict_type", dictType.getDictType()));
        if (a && b) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }

}
