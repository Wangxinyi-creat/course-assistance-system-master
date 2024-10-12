package com.zhedian.provide.system.model.vo.menu;

import lombok.Data;

import java.util.List;

@Data
public class RouterMenuVo {

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径(位置)
     */
    private String component;

    private Meta meta;

    private List<RouterMenuVo> children;
}
