import request from '@/utils/request';

/**
 * 获取字典数据(类型+数据)
 * @param params
 * @returns {*}
 */
export function getDict(params) {
    return request({
        url: 'dict/get',
        method: 'get',
        params,
    });
}


/**
 * 获取字典(通过字典类型获取数据)
 * @param dictType
 * @returns {*}
 */
export function getByDictType(dictType) {
    return request({
        url: 'dict/getByDictType',
        method: 'get',
        params: {
            dictType: dictType
        },
    });
}

/**
 * 添加字典数据
 * @param data
 * @returns {*}
 */
export function addDict(data) {
    return request({
        url: 'dict/add',
        method: 'post',
        data: data,
    });
}

/**
 * 修改字典数据
 * @param data
 * @returns {*}
 */
export function updateDict(data) {
    return request({
        url: 'dict/update',
        method: 'post',
        data: data,
    });
}

/**
 * 删除字典数据
 * @param data
 * @returns {*}
 */
export function deleteDict(data) {
    return request({
        url: 'dict/delete',
        method: 'post',
        data: data,
    });
}