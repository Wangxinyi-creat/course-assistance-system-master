import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import directive from './directive' // directive

import Particles from 'vue-particles';
import Element from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/theme/index.scss';
import iconPicker from 'e-icon-picker'; //全局引入eiconPicker图标库
import 'e-icon-picker/lib/index.css'; // eiconPicker基本样式，包含基本图标
import "e-icon-picker/lib/symbol.js"; //eiconPicker基本彩色图标库
import 'font-awesome/css/font-awesome.min.css'; //font-awesome 图标库
import 'element-ui/lib/theme-chalk/index.css'; //elementUi全局样式

import {globalComponentSize} from '@/utils/style/componentSize.js';
import {getDictLabel} from "@/utils/dictUtil";

Vue.use(directive) //自定义全局指令
Vue.use(Particles);
Vue.use(Element, {size: globalComponentSize});
Vue.use(iconPicker, {
    ElementUI: true, //elementUi 图标库
    FontAwesome: false,//是否开启font-awesome 图标库
    eIcon: false,
    eIconSymbol: false //是否开启彩色图标
});
Vue.config.productionTip = false;
Vue.prototype.bus = new Vue();
Vue.prototype.$getDictLabel = getDictLabel

new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');
