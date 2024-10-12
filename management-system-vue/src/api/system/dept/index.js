import request from '@/utils/request';


/**
 * 获取全部部门
 * @returns {*}
 */
export function getDeptList(params) {
    return request({
        url: 'dept/get',
        method: 'get',
        params
    });
}


/**
 * 删除部门
 * @param id
 * @returns {*}
 */
export function deleteDept(id) {
    return request({
        url: 'dept/delete/' + id,
        method: 'delete',
    });
}

/**
 * 添加部门
 * @param data
 * @returns {*}
 */
export function addDept(data) {
    return request({
        url: 'dept/add',
        method: 'post',
        data: data,
    });
}

/**
 * 修改部门
 * @param data
 * @returns {*}
 */
export function updateDept(data) {
    return request({
        url: 'dept/update',
        method: 'PUT',
        data: data,
    });
}

