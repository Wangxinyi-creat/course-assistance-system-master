/**
 * 添加路由工具类
 */
import {addRouterByVue} from "@/router";

/**
 * 添加、修改通知公告路由
 */
export function addOrEditNoticeRouter() {
    const addRouters = {
        name: "addOrEdit-notice",
        path: "/addOrEdit-notice",
        component: () => import('@/views/system/notice/addOrEdit-notice.vue'),
        meta: {
            title: "添加/修改公告",
            isLink: null,
            isIframe: false,
            isHide: false,
            isAffix: false
        }
    }
    addRouterByVue(addRouters)
}

/**
 * 查询公告详情路由
 */
export function CheckNoticeRouter() {
    const addRouters = {
        name: "checkNotice",
        path: "/checkNotice",
        component: () => import('@/views/system/notice/checkNotice.vue'),
        meta: {
            title: "查看公告",
            isLink: null,
            isIframe: false,
            isHide: false,
            isAffix: false
        }
    }
    addRouterByVue(addRouters)
}

/**
 * 角色授权用户页面路由
 */
export function addEmpowerRouter() {
    const addRouters = {
        name: "empower",
        path: "/empower",
        component: () => import('@/views/system/role/empower.vue'),
        meta: {
            title: "查询已授权用户",
            isLink: null,
            isIframe: false,
            isHide: false,
            isAffix: false
        }
    }
    addRouterByVue(addRouters)
}
