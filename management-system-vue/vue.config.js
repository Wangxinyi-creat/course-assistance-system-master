module.exports = {
    productionSourceMap: false,
    // publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
    // transpileDependencies: true,
    lintOnSave: false,
    // 跨域配置
    devServer: {                //记住，别写错了devServer//设置本地默认端口  选填
        port: 8081,
        proxy: {                 //设置代理，必须填
            '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
                target: 'http://localhost:8081',     //代理的目标地址
                changeOrigin: true,              //是否设置同源，输入是的
                pathRewrite: {                   //路径重写
                    '^/api': ''                     //选择忽略拦截器里面的内容
                }
            },
        }
    },
    chainWebpack(config) {
        config.plugins.delete('preload');
        config.plugins.delete('prefetch');
    },
    css: {
        extract: {ignoreOrder: true},
    },
};
