package com.zhedian.admin.exception;

import com.zhedian.common.domain.CommonResult;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.ConnectException;
import java.sql.SQLIntegrityConstraintViolationException;


@ResponseBody
@ControllerAdvice
public class ExceptionAdvice {
    public static Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(ConnectException.class)
    public CommonResult ConnectException(Exception e) {
        logger.error("============全局异常捕获---开始" + "===============");
        logger.error("连接异常");
        logger.error(e.getMessage());
        logger.error("============全局异常捕获---结束" + "===============");
        return CommonResult.failed("服务器连接异常,请联系管理员处理");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public CommonResult SQLIntegrityConstraintViolationException(Exception e) {
        logger.error("============全局异常捕获---开始" + "===============");
        logger.error("字段名称重复");
        logger.error(e.getMessage());
        logger.error("============全局异常捕获---结束" + "===============");
        return CommonResult.failed("错误！请检查是否有重复数据！");
    }


    @ExceptionHandler(FileSizeLimitExceededException.class)
    public CommonResult FileSizeLimitExceededException(Exception e) {
        logger.error("============全局异常捕获---开始" + "===============");
        logger.error("上传文件过大,请重新上传");
        logger.error(e.getMessage());
        logger.error("============全局异常捕获---结束" + "===============");
        return CommonResult.failed("上传文件过大,请重新上传");
    }

}
