import request from '@/utils/request';

/**
 * 获取路由
 * @returns {{getRouterMenu: (function(*): AxiosPromise<any>)}}
 */
export function useMenuApi() {
    return {
        getRouterMenu: (params) => {
            // 本地数据，路径：`/public/xxx.json`
            return request({
                url: 'menu/getRouterMenu',
                method: 'get',
                params,
            });
        },
    };
}

/**
 * 查询菜单
 * @returns {*}
 */
export function getMenuList(params) {
    return request({
        url: 'menu/get',
        method: 'get',
        params
    });
}

/**
 * 获取当前用户菜单
 * @returns {*}
 */
export function getUserMenu() {
    return request({
        url: 'menu/getUserMenu',
        method: 'get',
    });
}

/**
 * 添加菜单
 * @param data
 * @returns {*}
 */
export function addMenu(data) {
    return request({
        url: 'menu/add',
        method: 'post',
        data: data,
    });
}

/**
 * 修改菜单
 * @param data
 * @returns {*}
 */
export function updateMenu(data) {
    return request({
        url: 'menu/update',
        method: 'post',
        data: data,
    });
}

/**
 * 删除菜单
 * @param id
 * @returns {*}
 */
export function deleteMenu(id) {
    return request({
        url: 'menu/delete/' + id,
        method: 'delete',
    });
}

/**
 * 获取菜单--供角色分配菜单时使用
 * @returns {*}
 */
export function getMenuToRole() {
    return request({
        url: 'menu/getMenuToRole',
        method: 'get',
    });
}

/**
 * 获取角色菜单
 * @param roleId
 * @returns {*}
 */
export function getRoleMenu(roleId) {
    return request({
        url: 'menu/getByRoleId',
        method: 'get',
        params: {
            id: roleId
        },
    });
}
