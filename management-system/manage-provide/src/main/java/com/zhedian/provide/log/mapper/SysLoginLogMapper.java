package com.zhedian.provide.log.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhedian.provide.log.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志Mapper接口
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
