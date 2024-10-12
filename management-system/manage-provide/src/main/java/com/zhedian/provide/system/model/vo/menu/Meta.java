package com.zhedian.provide.system.model.vo.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {

    private String title;

    /**
     * 外部链接
     */
    private String isLink;

    /**
     * 是否内嵌(0-否   1-是)
     */
    private Boolean isIframe;

    /**
     * 是否隐藏tagsView(0-否   1-是)
     */
    private Boolean isHide;

    /**
     * 是否固定tagsView(0-否   1-是)
     */
    private Boolean isAffix;

//    /**
//     * 是否缓存
//     */
//    private Boolean isKeepAlive;

    /**
     * 菜单图标(element-ui 图标样式)
     */
    private String icon;
}
