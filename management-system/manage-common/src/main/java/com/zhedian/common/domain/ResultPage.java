package com.zhedian.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页数据封装类
 */
@Data
public class ResultPage<T> {

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 一页多少条数据
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总数据数量
     */
    private Long total;
    /**
     * 当前页数据
     */
    private List<T> list;

    /**
     * 将MyBatis Plus 分页结果转化为通用结果
     */
    public static <T> ResultPage<T> restPage(Page<T> pageResult) {
        ResultPage<T> result = new ResultPage<>();
        // 当前页
        result.setPageNum((int) (pageResult.getCurrent()));
        // 一页多少条数据
        result.setPageSize((int) (pageResult.getSize()));
        // 总页数
        result.setTotalPage((int) (pageResult.getTotal() / pageResult.getSize() + 1));
        // 总数据数量
        result.setTotal(pageResult.getTotal());
        // 当前页数据
        result.setList(pageResult.getRecords());
        return result;
    }
}
