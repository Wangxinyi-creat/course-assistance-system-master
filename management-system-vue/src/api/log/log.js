import request from '@/utils/request';

/**
 * 获取登录日志
 * @param params
 * @returns {*}
 */
export function getLogin(params) {
    return request({
        url: 'log/login/get',
        method: 'get',
        params,
    });
}


/**
 * 获取系统操作日志
 * @param params
 * @returns {*}
 */
export function getOperate(params) {
    return request({
        url: 'log/operate/get',
        method: 'get',
        params,
    });
}