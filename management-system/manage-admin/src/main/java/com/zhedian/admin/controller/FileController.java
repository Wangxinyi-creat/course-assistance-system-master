package com.zhedian.admin.controller;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zhedian.common.domain.CommonResult;
import com.zhedian.common.domain.CustomExceptionResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传接口
 */
@Api(tags = "文件管理")
@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 对应公网endpoint地址
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    /**
     * 账号
     */
    @Value("${aliyun.oss.keyId}")
    private String ACCESS_KEY_ID;
    /**
     * 密码
     */
    @Value("${aliyun.oss.keySecret}")
    private String ACCESS_KEY_SECRET;
    /**
     * 对应要存储到哪个块
     */
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 块下的文件路径
     */
    @Value("${aliyun.oss.datePath}")
    private String datePath;

    @Value("${uploadPath}")
    private String uploadPath;

    @ApiOperation("文件上传-OSS")
    @PostMapping("/upload")
    public CommonResult uploadFile(@RequestBody MultipartFile file) {
        //获取文件后缀
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //判断上传文件是否图片
        String path = "/file";
        if (fileSuffix.equals(".jpg") || fileSuffix.equals(".png")) {
            path = "/img";
        }
        //获取时间戳，作为文件名称路径
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String timeStamp = simpleDateFormat.format(new Date());
        path = path + "/" + timeStamp;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            //新的文件名   uuid+后缀
            String fileName = IdUtil.simpleUUID() + fileSuffix;
            //拼接 manage/img?file/时间(20230724)/***.***
            String originalFilename = datePath + path + "/" + fileName;
            // oss实现上传文件
            //第一个参数：Bucket名称
            //第二个参数：上传到oss文件路径和文件名称  如/manage/img?file/admin.jpg  同名覆盖
            ossClient.putObject(bucketName, originalFilename, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来->https://hangzhou.aliyuncs.com/wms/admin(时间戳).jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;

            return CommonResult.success(url, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation("文件上传-本地")
    @PostMapping("/uploadLocal")
    public CommonResult uploadFileLocal(@RequestBody MultipartFile file) throws IOException {
        //文件不为空
        if (file.isEmpty()) {
            throw new CustomExceptionResult("上传文件不能为空");
        }

        //获取文件名称
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        //判断上传文件是否图片
        String filePath = "/file/";
        if (fileSuffix.equals(".jpg") || fileSuffix.equals(".png")) {
            filePath = "/img/";
        }

        String path = uploadPath + filePath;
        //真实上传路径是否存在，不存在创建该路径
        File fileRealPath = new File(path);
        if (!fileRealPath.exists()) {
            fileRealPath.mkdirs();
        }

        //时间格式化格式--获取当前时间并作为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = simpleDateFormat.format(new Date());
        //获取新文件名称   时间_原文件名称
        String newFileName = timeStamp + "_" + fileName;
        //文件保存    路径+文件名称
        file.transferTo(new File(fileRealPath, newFileName));

        //生成相对路径返回
        String url = filePath + newFileName;

        return CommonResult.success(url, "上传成功");
    }

}
