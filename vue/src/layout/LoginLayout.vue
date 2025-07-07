<script setup>
</script>

<template>
  <div class="login-layout">
    <el-main class="login-main">
      <div class="login-content">
        <router-view></router-view>
      </div>
    </el-main>
  </div>
</template>

<style scoped>
/* 主布局容器 */
.login-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(to bottom, rgba(122, 143, 251, 0.63), rgba(235, 178, 250, 0.52)); /* 渐变背景 */
}

.login-footer {
  text-align: center;
  padding: 10px 0;
  background: linear-gradient(to bottom, rgba(169, 204, 241, 0.3), rgba(244, 244, 244, 0.8));
  position: fixed;
  bottom: 0;
  width: 100%;
  color: #666;
  backdrop-filter: blur(5px);
}
</style>
