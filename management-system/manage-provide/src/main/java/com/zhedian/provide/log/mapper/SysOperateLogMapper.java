package com.zhedian.provide.log.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhedian.provide.log.entity.SysOperateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统操作日志Mapper接口
 */
@Mapper
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

}
