import request from '/src/utils/request';

/**
 * 登录api接口集合
 * @method signIn 用户登录
 * @method signOut 用户退出登录
 */


export function signIn(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data: data,
    });
}

export function getCaptcha() {
    return request({
        url: '/user/captcha',
        method: 'get'
    });
}

export function signOut() {
    return request({
        url: '/logout',
        method: 'post'
    });
}
