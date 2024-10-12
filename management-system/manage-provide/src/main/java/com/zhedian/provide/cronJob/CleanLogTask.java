package com.zhedian.provide.cronJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhedian.provide.log.service.ISysLoginLogService;
import com.zhedian.provide.log.service.ISysOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 清理日志-定时任务
 */
@EnableScheduling//开启定时任务
@Component
@Slf4j
public class CleanLogTask {

    @Resource
    private ISysLoginLogService loginLogService;

    @Resource
    private ISysOperateLogService operateLogService;


    @Scheduled(cron = "0 0 0 */10 * ?")//每10天执行一次
    public void cron() {
        log.info("==================定时清理日志任务开始==============");
        //清理登录日志
        loginLogService.remove(new QueryWrapper<>());
        //清理操作日志
        operateLogService.remove(new QueryWrapper<>());
        log.info("==================定时清理日志任务结束==============");
    }


}
