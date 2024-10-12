import request from '@/utils/request';

/**
 * 查询用户
 * @param params
 * @returns {*}
 */
export function getUser(params) {
    return request({
        url: 'user/get',
        method: 'get',
        params,
    });
}

/**
 * 添加用户
 * @param data
 * @returns {*}
 */
export function addUser(data) {
    return request({
        url: 'user/add',
        method: 'post',
        data: data,
    });
}

/**
 * 注册用户
 * @param data
 * @returns {*}
 */
export function register(data) {
    return request({
        url: 'user/register',
        method: 'post',
        data: data,
    });
}

/**
 * 修改用户
 * @param data
 * @returns {*}
 */
export function updateUser(data) {
    return request({
        url: 'user/update',
        method: 'post',
        data: data,
    });
}

/**
 * 删除用户
 * @param id
 * @returns {*}
 */
export function deleteUser(id) {
    return request({
        url: 'user/delete/' + id,
        method: 'delete',
    });
}

/**
 * 修改用户状态    是否可以登录
 * @param id
 * @param status
 * @returns {*}
 */
export function changeStatus(id, status) {
    return request({
        url: 'user/changeStatus/' + id + "/" + status,
        method: 'POST',
    });
}

/**
 * 修改个人信息
 * @param data
 * @returns {*}
 */
export function updatePersonal(data) {
    return request({
        url: 'user/updatePersonal',
        method: 'post',
        data: data,
    });
}

/**
 * 修改密码
 * @param id
 * @param data
 * @returns {*}
 */
export function updatePwd(id, data) {
    return request({
        url: 'user/updatePwd/' + id,
        method: 'post',
        data: data,
    });
}

/**
 * By角色id查询用户
 * @param params
 * @returns {*}
 */
export function getUserByRoleId(params) {
    return request({
        url: 'user/getUserByRoleId',
        method: 'get',
        params,
    });
}

/**
 * 取消角色授权
 * @param data
 * @returns {*}
 */
export function cancelUserRole(data) {
    return request({
        url: 'user/cancelUserRole',
        method: 'post',
        data: data,
    });
}
