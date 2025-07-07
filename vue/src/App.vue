<template>
  <component :is="currentLayout">
    <router-view></router-view>
  </component>
</template>

<script>
import AdminLayout from './layout/AdminLayout.vue';
import UserLayout from './layout/UserLayout.vue';
import LoginLayout from './layout/LoginLayout.vue';

export default {
  data() {
    return {
      currentLayout: this.getLayout(), // 获取当前布局
    };
  },
  methods: {
    getLayout() {
      if (this.$route.path === '/' || this.$route.path === '/register') return LoginLayout; // 登录页面使用独立布局
      // 根据角色决定加载哪个布局
      const role = localStorage.getItem('userRole');
      if (role === 'admin') return AdminLayout;
      else if (role === 'user') return UserLayout;

      // 默认返回登录布局（防止角色未设置时的异常）
      return LoginLayout;
    },
  },
  watch: {
    $route() {
      this.currentLayout = this.getLayout(); // 路由变化时切换布局
    },
  },
};
</script>
