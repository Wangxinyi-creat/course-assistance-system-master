package com.zhedian.provide.system.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.system.entity.SysNotice;
import com.zhedian.provide.system.mapper.SysNoticeMapper;
import com.zhedian.provide.system.service.ISysNoticeService;
import org.springframework.stereotype.Service;


/**
 * 通知公告Service业务层处理
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

}
