import Vue from 'vue';
import App from './App.vue';
import router from './router';
import VueResource from '../node_modules/vue-resource/dist/vue-resource.min.js';
import '../node_modules/jquery/dist/jquery.min.js';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap/dist/js/bootstrap.min.js';
import '../node_modules/js-md5/build/md5.min.js';
import '../public/public.css';
import flags from './scripts/flags.js';

Vue.use(VueResource);
Vue.config.productionTip = false;

Vue.prototype.$flags = flags;

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
