package com.zhedian.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据解析监听器， 数据解析方法异步执行
 *
 * @param <T> Excel中数据的类型
 */
@Getter
@Setter
@NoArgsConstructor
public class ExcelListener<T, E> extends AnalysisEventListener<T> {

    // 每次最多导入条数
    private final int batchSize = 2000;

    private List<E> entityList;

    private Class<E> entityClass;

    private E entity;

    private IService<E> service;

    public ExcelListener(IService<E> service, Class<E> entityClass) {
        this.service = service;
        this.entityList = new ArrayList<>();
        this.entityClass = entityClass;
    }


    /**
     * Excel每解析一行数据，就会调用一次该方法
     * <p>
     * 获取解析后的数据集合， 如果数据还没有被解析完成，会对读取该集合的线程进行阻塞，直到数据读取完成之后，进行解锁。
     * 如果一次导入数据超过batchSize条，则先导入数据库中
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        entity = null;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        BeanUtils.copyProperties(data, entity);
        entityList.add(entity);
        if (entityList.size() > batchSize) {
            //数据保存进数据库
            service.saveBatch(entityList);
            // 手动清空数据内存数据，减少内存消耗
            entityList.clear();
        }
    }

    /**
     * 流中的数据解析完成之后，就会调用此方法
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        service.saveBatch(entityList);
    }

    /**
     * 解析过程如果发生异常，会调用此方法
     *
     * @param exception
     * @param context
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        throw new RuntimeException("Excel数据异常，请检查或联系管理员！" );
    }
}
