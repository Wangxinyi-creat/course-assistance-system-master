package com.zhedian.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zhedian.provide.common.AuthService;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mp自动填充
 */
@Component
public class DateConfig implements MetaObjectHandler {

    /**
     * 使用mp做添加操作时候，这个方法执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        String userName = AuthService.getUser().getUserName();
        this.setFieldValByName("createBy", userName, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userName, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 使用mp做修改操作时候，这个方法执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String userName = AuthService.getUser().getUserName();
        this.setFieldValByName("updateBy", userName, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
