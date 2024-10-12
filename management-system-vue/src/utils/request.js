import axios from 'axios'
import {Message} from "element-ui";
import {Session} from "@/utils/storage";
import router, {resetRouter} from "@/router";
import store from "@/store";

const request = axios.create({
    baseURL: '/api',  // 注意！！ 这里是全局统一加上了 '/api' 前缀，也就是说所有接口都会加上'/api'前缀在，页面里面写接口的时候就不要加 '/api'了，否则会出现2个'/api'，类似 '/api/api/user'这样的报错，切记！！！
    timeout: 1000 * 180
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    //获取token
    let token = Session.get('token')
    if (token) {
        //将token放到请求头发送给服务器
        //这里经常搭配token使用，将token值配置到tokenkey中，将tokenkey放在请求头中
        // config.headers['token'] = Token;　　　　
        // config.headers.token的token是需要和后台定义叫什么的，我这里定义叫token了
        // config.headers.common['Authorization'] = `${Session.get('token')}`;
        config.headers.token = token;
    }
    return config;
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        const that = this
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob'
            || response.config.responseType === 'arraybuffer'
            || response.config.responseType === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8') {
            return response
        }
        //查询返回code判断
        if (res.code !== 200) {
            // 403 登录过期
            if (res.code === 403) {
                // 清除浏览器全部临时缓存
                Session.clear();
                router.push('/login');
                //清除vuex中的路由信息
                store.dispatch('routesList/setRoutesList', {});
                // store.commit('setMenuData', {});
                resetRouter(); // 重置路由
                //提示
                Message.info("登录过期，请重新登录");
                return res
            }
            if (res.code === 401) {
                router.push('/401');
            } else if (res.code === 404) {
                router.push('/404');
            } else {
                Message.error(res.message);
            }
            return res;
        }
        return res;
    },
    error => {
        console.log('err' + error.response.status) // for debug
        // 对响应错误做点什么
        if (error.message.indexOf('timeout') != -1) {
            Message.error('网络超时');
        } else if (error.message == 'Network Error') {
            Message.error('网络连接错误');
        }
        if (error.response.status === 400) {
            Message.error("请求出错");
        }

        return Promise.reject(error);
    }
)


export default request
