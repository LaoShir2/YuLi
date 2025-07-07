<template>
  <div class="admin-layout">
    <el-header class="header">
      <el-menu 
        mode="horizontal" 
        :default-active="activeMenu"
        class="nav-menu"
        background-color="#E6F3FF"
        text-color="#409EFF"
        active-text-color="#1890FF">
        <el-menu-item index="1" @click="goAdmin">
          <el-icon><Monitor /></el-icon>
          <span>后台管理</span>
        </el-menu-item>
        <!-- <el-menu-item index="2" @click="goUsers">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item> -->
      </el-menu>
    </el-header>
    <el-main class="main-content">
      <router-view></router-view>
    </el-main>
  </div>
</template>

<script>
import { Monitor } from '@element-plus/icons-vue'
// import { User } from '@element-plus/icons-vue'

export default {
  components: {
    Monitor,
    // User
  },
  data() {
    return {
      activeMenu: this.getActiveMenu()
    };
  },
  methods: {
    goAdmin() {
      this.$router.push('/admin');
    },
    // goUsers() {
    //   this.$router.push('/admin/users');
    // },
    getActiveMenu() {
      const path = this.$route.path;
      if (path === '/admin') return '1';
      // if (path === '/admin/users') return '2';
      return '1';
    }
  },
  watch: {
    $route() {
      this.activeMenu = this.getActiveMenu();
    }
  }
};
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
  position: fixed;
  width: 100%;
  z-index: 999;
}

.nav-menu {
  display: flex;
  justify-content: flex-start;
  padding-left: 20px;
}

.nav-menu .el-menu-item {
  font-size: 14px;
  height: 50px;
  line-height: 50px;
  padding: 0 15px;
}

.nav-menu .el-menu-item:hover {
  background-color: #BAE7FF !important;
}

.main-content {
  padding-top: 60px;
  min-height: calc(100vh - 50px);
  max-width: 100%;
  margin: 0 auto;
}

.el-icon {
  margin-right: 3px;
  font-size: 16px;
}
</style>
