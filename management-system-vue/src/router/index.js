import Vue from 'vue';
import store from '../store';
import VueRouter from 'vue-router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import {Session} from '@/utils/storage';
import {PrevLoading} from '@/utils/style/loading.js';
import {useMenuApi} from '@/api/system/menu';

const menuApi = useMenuApi();

// 解决 `element ui` 导航栏重复点菜单报错问题
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch((err) => err);
};

// 安装 VueRouter 插件
Vue.use(VueRouter);

//设置动态路由默认值
const defalutRoutes = [
    {
        path: '/admin',
        name: 'admin',
        component: 'layout/index',
        redirect: '/personal',
        meta: {
            isKeepAlive: true,
        },
        children: [
            {
                name: "personal",
                path: "/personal",
                component: 'personal',
                meta: {
                    title: "个人中心",
                    isLink: null,
                    isIframe: false,
                    isHide: false,
                    isAffix: true,
                    icon: "iconfont icon-gerenzhongxin"
                }
            }
        ],
    },
];

// 定义静态路由
const staticRoutes = [
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login'),
        meta: {
            title: '登录',
        },
    },
    {
        path: '/404',
        name: 'notFound',
        component: () => import('@/views/error/404.vue'),
        meta: {
            title: 'message.staticRoutes.notFound',
        },
    },
    {
        path: '/401',
        name: 'noPower',
        component: () => import('@/views/error/401.vue'),
        meta: {
            title: 'message.staticRoutes.noPower',
        },
    },
    {
        path: '/front',
        name: '/front',
        component: () => import('@/views/front/index.vue'),
        redirect: '/frontHome',
        meta: {
            isKeepAlive: true,
        },
        children: [
            {
                name: "frontHome",
                path: "/frontHome",
                component: () => import('@/views/front/home/index.vue'),
                meta: {
                    title: "首页",
                    icon: "iconfont icon-gerenzhongxin"
                }
            },
            {
                name: "front-my",
                path: "/front-my",
                component: () => import('@/views/front/my/index.vue'),
                meta: {
                    title: "我的",
                    icon: "iconfont icon-gerenzhongxin"
                }
            }
        ]
    }
];

// 加载静态路由
const createRouter = () =>
    new VueRouter({
        routes: staticRoutes,
    });

// 创建路由
const router = createRouter();

// 加载 loading
PrevLoading.start();

// 添加路由，模拟数据与方法，可自行进行修改 test
// 添加动态路由，`{ path: '*', redirect: '/404' }` 防止页面刷新，静态路由丢失问题
export function addRouterUser(router, to, next) {
    resetRouter();
    menuApi
        .getRouterMenu()
        .then(async (res) => {
            //重新赋值动态路由
            const dynamicRoutes = defalutRoutes
            //数据添加至动态路由数组
            res.data.forEach(item => {
                dynamicRoutes[0].children.push(item)
            });
            // 读取用户信息 保存路由信息至vuex
            await store.dispatch('userInfos/setUserInfos');
            await store.dispatch('routesList/setRoutesList', dynamicRoutes[0].children);
            //数据转换
            const awaitRoute = await dynamicRouter(dynamicRoutes);
            //添加路由
            [...awaitRoute, {path: '*', redirect: '/404'}].forEach((route) => {
                router.addRoute({...route});
            });
            setCacheTagsViewRoutes(JSON.parse(JSON.stringify(dynamicRoutes[0].children)));
            next({...to, replace: true});
        })
        .catch(() => {
        });
}

// 重置路由
export function resetRouter() {
    router.matcher = createRouter().matcher;
}

// 递归处理每一项 `component` 中的路径
export function dynamicRouter(routes) {
    return routes.map((view) => {
        if (view.component) view.component = loadView(view.component);
        if (view.children) dynamicRouter(view.children);
        return view;
    });
}

// 处理后端返回的 `component` 路径，拼装实现懒加载
export function loadView(path) {
    /**
     * 打包成多个 js、多个 css
     */
    if (path.indexOf('layout') > -1) return () => import(`@/${path}`);
    else return () => import(`@/views/${path}`);
}

// 缓存多级嵌套数组处理后的一维数组(tagsView、菜单搜索中使用：未过滤隐藏的(isHide))
export function setCacheTagsViewRoutes(arr) {
    // 添加到 vuex setTagsViewRoutes 中
    store.dispatch('tagsViewRoutes/setTagsViewRoutes', formatFlatteningRoutes(arr));
}

// 多级嵌套数组处理成一维数组
export function formatFlatteningRoutes(arr) {
    if (arr.length <= 0) return false;
    for (let i = 0; i < arr.length; i++) {
        if (arr[i].children) {
            arr = arr.slice(0, i + 1).concat(arr[i].children, arr.slice(i + 1));
        }
    }
    return arr;
}


// 延迟关闭进度条
export function delayNProgressDone(time = 300) {
    setTimeout(() => {
        NProgress.done();
    }, time);
}

// 路由加载前
router.beforeEach((to, from, next) => {
    NProgress.configure({showSpinner: false});
    if (to.meta.title && to.path !== '/login') NProgress.start();
    let token = Session.get('token');
    if (to.path.includes('front')) {
        console.log('front')
        NProgress.start();
        next();
        delayNProgressDone();
    } else if (to.path === '/login' && !token) {
        NProgress.start();
        next();
        delayNProgressDone();
    } else {
        if (!token) {
            NProgress.start();
            next('/login');
            Session.clear();
            delayNProgressDone();
        } else if (token && to.path === '/login') {
            next('/home');
            delayNProgressDone();
        } else {
            if (Object.keys(store.state.routesList.routesList).length <= 0) {
                if (!Session.get('userInfo')) return false;
                //动态渲染路由
                addRouterUser(router, to, next);
            } else {
                next();
                delayNProgressDone(0);
            }
        }
    }
});


/**
 * 添加路由
 * @param config   路由配置
 * @returns {Promise<void>}
 */
export async function addRouterByVue(config) {
    //添加路由
    router.addRoute("admin", config)
    //获取vuex中的路由信息
    const tagsViewRoutes = store.state.tagsViewRoutes.tagsViewRoutes
    tagsViewRoutes.push(config)
    // 保存路由信息至vuex
    await store.dispatch('userInfos/setUserInfos');
    await store.dispatch('routesList/setRoutesList', tagsViewRoutes)
    setCacheTagsViewRoutes(JSON.parse(JSON.stringify(tagsViewRoutes)))
}

/**
 * 删除路由
 * @param name   路由name属性
 * @returns {Promise<void>}
 */
export async function deleteRouterByVue(name) {
    //获取vuex中的路由信息
    const tagsViewRoutes = store.state.tagsViewRoutes.tagsViewRoutes
    tagsViewRoutes.map((v, k, arr) => {
        if (v.name === name) {
            tagsViewRoutes.splice(k, 1);
        }
    });
    // 保存路由信息至vuex
    await store.dispatch('userInfos/setUserInfos');
    await store.dispatch('routesList/setRoutesList', tagsViewRoutes)
    setCacheTagsViewRoutes(JSON.parse(JSON.stringify(tagsViewRoutes)))
}

// 路由加载后
router.afterEach(() => {
    PrevLoading.done();
    delayNProgressDone();
});

// 导出路由
export default router;
