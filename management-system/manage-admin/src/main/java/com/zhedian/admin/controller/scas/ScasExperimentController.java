package com.zhedian.admin.controller.scas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.zhedian.common.annotation.LogOperation;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.ResultPage;
import com.zhedian.common.util.JavaStringExecution;
import com.zhedian.provide.common.AuthService;
import com.zhedian.provide.scas.entity.ScasCourse;
import com.zhedian.provide.scas.entity.ScasExperiment;
import com.zhedian.provide.scas.entity.ScasMyCourses;
import com.zhedian.provide.scas.service.IScasCourseService;
import com.zhedian.provide.scas.service.IScasMyCoursesService;
import com.zhedian.provide.scas.service.ScasExperimentService;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 实验报告表
 */
@Api(tags = "实验报告管理")
@RestController
@RequestMapping("/scas/experiment")
public class ScasExperimentController {

    @Autowired
    private ScasExperimentService scasExperimentService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private IScasMyCoursesService scasMyCoursesService;
    @Autowired
    private IScasCourseService scasCourseService;

    @GetMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> generatePdf(@RequestParam Integer id) throws Exception {

        // 创建一个ByteArrayOutputStream来存储PDF数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 创建一个PdfWriter实例，它将数据写入ByteArrayOutputStream
        PdfWriter writer = new PdfWriter(baos);
        // 创建一个PdfDocument实例，并将PdfWriter作为参数传递
        PdfDocument pdf = new PdfDocument(writer);
        // 创建一个Document实例，它将内容添加到PdfDocument
        Document document = new Document(pdf);

        ScasExperiment experiment = scasExperimentService.getById(id);

        Paragraph paragraph = new Paragraph();
        paragraph.add(experiment.getExperimentTitle());
        paragraph.add(new Paragraph().add("").setMarginTop(10));
        paragraph.add(experiment.getExperimentDescription());
        paragraph.add(new Paragraph().add("").setMarginTop(10));
        paragraph.add(experiment.getMyCode());
        paragraph.add(new Paragraph().add("").setMarginTop(10));
        paragraph.add(experiment.getAnswer());
        paragraph.add(new Paragraph().add("").setMarginTop(10));
        paragraph.add(experiment.getUploadUrl());
        paragraph.add(new Paragraph().add("").setMarginTop(10));
        document.add(paragraph);

        // 关闭Document，这将触发渲染过程
        document.close();
        // 创建一个ByteArrayResource，它包装了包含PDF数据的byte数组
        Resource resource = new ByteArrayResource(baos.toByteArray());
        // 设置HTTP响应头，以便浏览器知道这是一个PDF文件并提示用户下载
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "generated-pdf.pdf");
        // 创建一个ResponseEntity实例，并将Resource作为body返回
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @ApiOperation("获取实验报告")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult<ResultPage<ScasExperiment>> getScasExperiment(@RequestParam Integer pageSize,
                                                                      @RequestParam Integer pageNum,
                                                                      @RequestParam(required = false) Integer courseId,
                                                                      @RequestParam(required = false) String userName,
                                                                      @RequestParam(required = false) String jobNumber,
                                                                      @RequestParam(required = false) String major) {
        Page<ScasExperiment> page = new Page<>(pageNum, pageSize);
        //条件构造器
        LambdaQueryWrapper<ScasExperiment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(courseId), ScasExperiment::getCourseId, courseId)
                .like(ObjectUtils.isNotNull(userName), ScasExperiment::getUserName, userName)
                .like(ObjectUtils.isNotNull(jobNumber), ScasExperiment::getJobNumber, jobNumber);
        if (ObjectUtils.isNotNull(major)) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(SysUser::getMajor, major);
            List<SysUser> sysUsers = userService.list(wrapper);
            List<String> collect = sysUsers.stream().map(SysUser::getUserName).collect(Collectors.toList());
            if (ObjectUtils.isNotNull(collect)) {
                queryWrapper.in(ScasExperiment::getUserName, collect);
            }
        }
        SysUser user = AuthService.getUser();
        //判断用户身份 查询学生的实验报告 或者老师的实验报告
        if (user.getIdentity().equals("学生")) {
            queryWrapper.eq(ScasExperiment::getUserName, user.getUserName());
        } else if (user.getIdentity().equals("老师")) {
            List<ScasCourse> courses = scasCourseService.list(new QueryWrapper<ScasCourse>().eq("teacher_id", user.getId()));
            List<Integer> collect = courses.stream().map(ScasCourse::getId).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(collect)) {
                queryWrapper.in(ScasExperiment::getCourseId, collect);
            } else {
                queryWrapper.eq(ScasExperiment::getId, 0);
            }
        }
        //分页查询
        Page<ScasExperiment> scasExperimentPage = scasExperimentService.page(page, queryWrapper);
        //查询课程名称
        if (ObjectUtils.isNotEmpty(scasExperimentPage.getRecords())) {
            for (ScasExperiment record : scasExperimentPage.getRecords()) {
                ScasCourse course = scasCourseService.getById(record.getCourseId());
                if (ObjectUtils.isNotNull(course)) record.setCourseName(course.getCourseName());
            }
        }
        return CommonResult.success(ResultPage.restPage(scasExperimentPage));
    }


    @ApiOperation("新增实验报告")
    @LogOperation("新增实验报告")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addScasExperiment(@RequestBody ScasExperiment scasExperiment) {
        // 查询报名课程下的学生
        List<ScasMyCourses> list = scasMyCoursesService.list(new LambdaQueryWrapper<ScasMyCourses>().eq(ScasMyCourses::getCourseId, scasExperiment.getCourseId()));
        List<String> collect = list.stream().map(ScasMyCourses::getUserName).collect(Collectors.toList());
        // 保存实验报告
        for (String userName : collect) {
            ScasExperiment experiment = new ScasExperiment();
            BeanUtils.copyProperties(scasExperiment, experiment);
            experiment.setUserName(userName);
            scasExperimentService.save(experiment);
        }
        return CommonResult.success("添加成功");
    }


    @ApiOperation("修改实验报告-状态")
    @LogOperation("修改实验报告-状态")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CommonResult updateScasExperiment(@RequestBody ScasExperiment scasExperiment) {
        //修改
        if (scasExperiment.getEditFlag() == 1) {
            scasExperiment.setUploadDate(new Date());
            ScasExperiment experiment = scasExperimentService.getById(scasExperiment.getId());
            String myCode = scasExperiment.getMyCode();
            String cute = JavaStringExecution.cute(myCode);
            cute = cute.replace("\r", "");
            cute = cute.replace("\n", "");
            if (!cute.equals(experiment.getRightResult())) {
                scasExperiment.setResult("错误");
            }
        }
        if (scasExperimentService.updateById(scasExperiment)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("修改实验报告")
    @LogOperation("修改实验报告")
    @RequestMapping(value = "/updateHandle", method = RequestMethod.PUT)
    public CommonResult updateHandle(@RequestBody ScasExperiment scasExperiment) {

        //删除
        deleteScasExperiment(scasExperiment.getId());
        //新增
        return addScasExperiment(scasExperiment);
    }

    @ApiOperation("删除实验报告")
    @LogOperation("删除实验报告")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult deleteScasExperiment(@PathVariable("id") Integer id) {
        ScasExperiment experiment = scasExperimentService.getById(id);
        // 查询同类实验报告
        LambdaQueryWrapper<ScasExperiment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScasExperiment::getCourseId, experiment.getCourseId())
                .eq(ScasExperiment::getExperimentTitle, experiment.getExperimentTitle())
                .eq(ScasExperiment::getExperimentDescription, experiment.getExperimentDescription());
        List<ScasExperiment> list = scasExperimentService.list(queryWrapper);
        for (ScasExperiment entity : list) {
            scasExperimentService.removeById(entity.getId());
        }
        return CommonResult.success("删除成功");
    }

}
