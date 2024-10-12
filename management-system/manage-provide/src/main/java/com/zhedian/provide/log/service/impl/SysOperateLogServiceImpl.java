package com.zhedian.provide.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.log.entity.SysOperateLog;
import com.zhedian.provide.log.mapper.SysOperateLogMapper;
import com.zhedian.provide.log.service.ISysOperateLogService;
import org.springframework.stereotype.Service;


/**
 * 系统操作日志Service业务层处理
 */
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements ISysOperateLogService {

}
