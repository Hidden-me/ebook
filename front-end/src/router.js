import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Library from './views/Library.vue'
import Cart from './views/Cart.vue'
import Management from './views/Management.vue'
import UserOrder from './views/UserOrder.vue'
import AccessDenied from "./views/AccessDenied.vue";
import DuplicateAuth from "./views/DuplicateAuth.vue";
import NotFound from "./views/NotFound.vue";

Vue.use(Router);

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/register',
            name: 'register',
            component: Register
        },
        {
            path: '/library',
            name: 'library',
            component: Library
        },
        {
            path: '/cart',
            name: 'cart',
            component: Cart
        },
        {
            path: '/management',
            name: 'management',
            component: Management
        },
        {
            path: '/order',
            name: 'order',
            component: UserOrder
        },
        {
            path: '/forbidden/access_denied',
            name: 'accessDenied',
            component: AccessDenied
        },
        {
            path: '/forbidden/duplicate_auth',
            name: 'duplicateAuth',
            component: DuplicateAuth
        },
        {
            path: '*',
            component: NotFound
        }
    ]
})
