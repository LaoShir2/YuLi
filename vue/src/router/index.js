import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import LoginPage from '../views/LoginPage.vue';
import PublishProduct from '../views/PublishProduct.vue';
import UserProfile from '../views/UserProfile.vue';
import ProductDetail from '../views/ProductDetail.vue';
import Admin from '@/views/AdminPage.vue';
import RegisterPage from '@/views/RegisterPage.vue';
import ChatPage from '@/views/ChatPage.vue';

const routes = [
    { path: '/', name: 'Login', component: LoginPage, meta: { requiresAuth: false } },
    { path: '/home', name: 'Home', component: HomePage, meta: { requiresAuth: true } }, // 需要认证
    { path: '/products/:id', name: 'ProductDetail', component: ProductDetail },
    { path: '/publish', name: 'PublishProduct', component: PublishProduct, meta: { requiresAuth: true } }, // 需要认证
    { path: '/profile', name: 'UserProfile', component: UserProfile, meta: { requiresAuth: true } }, // 需要认证
    { path: '/register', name: 'Register', component: RegisterPage, meta: { requiresAuth: false } }, // 不需要认证
    { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAuth: true } }, // 需要认证
    { path: '/chat', name: 'Chat', component: ChatPage, meta: { requiresAuth: true } }, // 需要认证
];


const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// 路由守卫：根据认证状态和角色重定向
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token'); // 获取 token
    const role = localStorage.getItem('userRole'); // 获取用户角色
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth); // 是否需要认证

    // 如果页面需要认证并且没有 token，跳转到登录页面
    if (requiresAuth && !token) {
        return next({ name: 'Login' });
    }

    // 如果已经登录，且访问 login 或 register 页面，跳转到首页
    if (token && (to.name === 'Login' || to.name === 'Register')) {
        return next({ name: 'Home' }); // 已登录，跳转到首页
    }

    // 如果用户是普通用户，禁止访问管理员页面
    if (to.path.startsWith('/admin') && role !== 'admin') {
        return next('/home'); // 重定向到普通用户主页
    }

    // 如果用户是管理员，禁止访问普通用户页面
    if (to.path.startsWith('/home') && role === 'admin') {
        return next('/admin'); // 重定向到管理员主页
    }

    // 页面刷新后，保存当前的路由路径
    if (to.name) {
        localStorage.setItem('currentRoute', to.fullPath);
    }

    next(); // 允许访问
});

// 页面刷新后恢复上次访问的路由
router.isReady().then(() => {
    const savedRoute = localStorage.getItem('currentRoute');
    if (savedRoute) {
        router.push(savedRoute); // 恢复上次访问的路由
    }
});

export default router;
