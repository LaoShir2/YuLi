<template>
  <div class="user-layout">
    <header class="header">
      <div class="header-container">
        <!-- Logo Section -->
        <div class="logo-section" @click="goHomeAndActivate">
          <img src="../assets/logo.png" class="logo-img" alt="logo">
          <h2 class="title">予礼</h2>
        </div>

        <!-- Navigation Menu -->
        <el-menu
            mode="horizontal"
            :default-active="activeMenu"
            class="nav-menu"
            background-color="#D6E9FF"
            text-color="#409EFF"
            active-text-color="#1890FF">
          <el-menu-item index="1" @click="goChat">
            <el-icon><ChatDotRound /></el-icon>
            <span>对话</span>
          </el-menu-item>
          <el-menu-item index="2" @click="goHome">
            <el-icon><House /></el-icon>
            <span>主页</span>
          </el-menu-item>
          <el-menu-item index="4" @click="goProfile">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </div>
    </header>
    
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script>
import {ChatDotRound, House, User} from '@element-plus/icons-vue'

export default {
  components: {
    ChatDotRound,
    House,
    User
  },
  data() {
    return {
      activeMenu: this.getActiveMenu()
    };
  },
  methods: {
    goHome() {
      this.$router.push('/home');
    },
    goPublish() {
      this.$router.push('/publish');
    },
    goProfile() {
      this.$router.push('/profile');
    },
    goChat() {
      this.$router.push('/chat');
    },
    goHomeAndActivate() {
      this.$router.push('/home');
      this.activeMenu = '2'; // 将激活菜单项设置为主页对应的索引
    },
    getActiveMenu() {
      const path = this.$route.path;
      if (path === '/chat') return '1';
      if (path === '/home') return '2';
      if (path === '/publish') return '3';
      if (path === '/profile') return '4';
      return '2';
    },
    handleScroll() {
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      const header = document.querySelector('.header');
      if (scrollTop > 100) {
        header.style.opacity = 0;
        header.style.transform = 'translateY(-100%)';
      } else {
        header.style.opacity = 1;
        header.style.transform = 'translateY(0)';
      }
    }
  },
  watch: {
    $route() {
      this.activeMenu = this.getActiveMenu();
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll);
  }
};
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, rgba(248, 234, 253, 0.27) 0%, #fdfdfd 100%);
}

.header {
  padding: 0;
  background: linear-gradient(to bottom, rgba(165, 126, 250, 0.37), rgba(217, 214, 255, 0.85));
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 999;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.header-container {
  display: flex;
  align-items: center;
  max-width: 80%;
  margin: 0 auto;
  height: 60px;
}

.logo-section {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 20px;
  transition: all 0.3s ease;
}

.logo-section:hover {
  transform: translateY(-1px);
}

.logo-img {
  width: 32px;
  height: auto;
  margin-right: 12px;
  transition: transform 0.3s ease;
}

.logo-section:hover .logo-img {
  transform: scale(1.05);
}

.title {
  background: linear-gradient(45deg, rgba(78, 41, 246, 0.9), rgba(118, 64, 255, 0.9));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 24px;
  margin: 0;
  font-weight: 600;
  transition: all 0.3s ease;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  border: none !important;
  background-color: transparent !important;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 14px;
  height: 60px;
  line-height: 60px;
  padding: 0 24px;
  border: none !important;
  color: #666;
  transition: all 0.3s ease;
}

.nav-menu :deep(.el-menu-item:hover) {
  background-color: rgba(77, 35, 253, 0.06) !important;
  color: rgba(165, 126, 250, 0.7) !important;
}

.nav-menu :deep(.el-menu-item.is-active) {
  background-color: rgba(77, 35, 253, 0.06) !important;
  color: rgba(115, 35, 253, 0.9) !important;
  border-bottom: 2px solid rgba(148, 35, 253, 0.63) !important;
  font-weight: 500;
}

.nav-menu :deep(.el-menu-item .el-icon) {
  margin-right: 6px;
  font-size: 16px;
  transition: transform 0.3s ease;
}

.nav-menu :deep(.el-menu-item:hover .el-icon) {
  transform: translateY(-1px);
}

.main-content {
  padding-top: 80px;
  min-height: calc(100vh - 60px);
  max-width: 80%;
  margin: 0 auto;
  transition: all 0.3s ease;
}

@media (max-width: 1200px) {
  .header-container,
  .main-content {
    max-width: 90%;
  }
}

@media (max-width: 768px) {
  .header-container,
  .main-content {
    max-width: 95%;
  }
  
  .nav-menu :deep(.el-menu-item) {
    padding: 0 15px;
  }
  
  .title {
    font-size: 20px;
  }
}
</style>