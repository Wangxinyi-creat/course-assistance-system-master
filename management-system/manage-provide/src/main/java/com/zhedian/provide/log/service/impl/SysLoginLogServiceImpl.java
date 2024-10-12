package com.zhedian.provide.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.log.entity.SysLoginLog;
import com.zhedian.provide.log.mapper.SysLoginLogMapper;
import com.zhedian.provide.log.service.ISysLoginLogService;
import org.springframework.stereotype.Service;


/**
 * 登录日志Service业务层处理
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

}
