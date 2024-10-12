import request from '@/utils/request';

/**
 * 查询接口
 * @param params
 * @returns {*}
 */
export function getRole(params) {
    return request({
        url: 'role/get',
        method: 'get',
        params,
    });
}

/**
 * 添加角色
 * @param data
 * @returns {*}
 */
export function addRole(data) {
    return request({
        url: 'role/add',
        method: 'post',
        data: data,
    });
}

/**
 * 修改角色
 * @param data
 * @returns {*}
 */
export function updateRole(data) {
    return request({
        url: 'role/update',
        method: 'post',
        data: data,
    });
}

/**
 * 删除角色
 * @param id
 * @returns {*}
 */
export function deleteRole(id) {
    return request({
        url: 'role/delete/' + id,
        method: 'delete',
    });
}

/**
 * 修改角色是否可见
 * @param id
 * @param showFlag
 * @returns {*}
 */
export function changeStatus(id, showFlag) {
    return request({
        url: 'role/changeStatus/' + id + "/" + showFlag,
        method: 'POST',
    });
}

/**
 *  修改角色菜单
 * @param roleId
 * @param data
 * @returns {*}
 */
export function addRoleMenu(roleId, data) {
    return request({
        url: 'role/addRoleMenu/' + roleId,
        method: 'post',
        data: data
    })
}

/**
 * 获取角色--供用户选择角色使用
 * @returns {*}
 */
export function getRoleToUser() {
    return request({
        url: 'role/getRoleToUser',
        method: 'get',
    });
}
