<template>
  <div class="register-wrapper">
    <div class="register-container">
      <div class="form-header">
        <h2>予礼</h2>
        <p>个性化礼物 AI 推荐平台</p>
      </div>
      <form @submit.prevent="register" class="floating-form">
        <div class="input-group">
          <input id="username" v-model.trim="user.name" type="text" autocomplete="off" @input="validateInput" required />
          <label for="username">用户名</label>
          <span class="highlight"></span>
        </div>
        <div class="input-group">
          <input id="password" v-model.trim="user.password" type="password" autocomplete="off" @input="validateInput" required />
          <label for="password">密码</label>
          <span class="highlight"></span>
        </div>
        <div class="input-group">
          <input id="email" v-model.trim="user.email" type="email" autocomplete="off" @input="validateInput" required />
          <label for="email">邮箱</label>
          <span class="highlight"></span>
        </div>
        <div class="error-message" v-if="errorMsg">{{ errorMsg }}</div>
        <button type="submit" class="submit-btn" :disabled="!isFormValid">
          <span>注册</span>
          <i class="arrow-icon"></i>
        </button>
        <div class="form-footer">
          <span>已有账号？</span>
          <a href="#" @click.prevent="goToLogin">立即登录</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { addUser } from "@/api";

export default {
  setup() {
    const router = useRouter();

    // 表单数据
    const user = reactive({
      name: '',
      password: '',
      email: ''
    });

    const errorMsg = ref('');
    const isFormValid = ref(false);

    // 输入验证
    const validateInput = () => {
      // 基本验证
      if (user.name && user.password && user.email) {
        isFormValid.value = true;
        errorMsg.value = '';
      } else {
        isFormValid.value = false;
      }
    };

    // 注册处理
    const register = async () => {
      // 防止XSS攻击
      const xssPattern = /(~|\{|\}|"|'|<|>|\?)/g;
      if (xssPattern.test(user.name) || xssPattern.test(user.password) || xssPattern.test(user.email)) {
        errorMessage('警告:输入内容包含非法字符');
        return;
      }

      try {
        // 对输入进行转义处理
        const safeName = encodeURIComponent(user.name);
        const safePassword = encodeURIComponent(user.password);
        const safeEmail = encodeURIComponent(user.email);

        // 实际的注册API调用
        await addUser({ name: safeName, password: safePassword, email: safeEmail });

        errorMessage('注册成功');
        setTimeout(() => {
          router.push({ name: 'Login' });
        }, 1000);
      } catch (error) {
        console.error('Failed to register:', error);
        errorMessage('注册失败');
        user.name = '';
        user.password = '';
        user.email = '';
      }
    };

    // 错误提示
    const errorMessage = (text) => {
      errorMsg.value = text;
      setTimeout(() => {
        errorMsg.value = '';
      }, 3000);
    };

    const goToLogin = () => {
      router.push({ name: 'Login' });
    };

    // 禁用滚动
    const disableScroll = () => {
      document.body.style.overflow = 'hidden';
    };

    // 启用滚动
    const enableScroll = () => {
      document.body.style.overflow = 'auto';
    };

    onMounted(() => {
      validateInput();
      disableScroll();
    });

    onUnmounted(() => {
      enableScroll();
    });

    return {
      user,
      errorMsg,
      isFormValid,
      validateInput,
      register,
      goToLogin
    };
  }
};
</script>

<style scoped>
/* 基础颜色变量定义 */

.register-wrapper {
  min-height: 93.5vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(243, 229, 245, 0);
  padding: 15px;
}

.register-container {
  width: 100%;
  max-width: 420px;
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.register-container:hover {
  transform: translateY(-5px);
}

.form-header {
  text-align: center;
  margin-bottom: 30px;
}

.form-header h2 {
  color: rgba(118, 64, 255, 0.89);
  font-size: 26px;
  margin-bottom: 8px;
  font-weight: 600;
}

/* 修改送礼AI小助手字体颜色为紫色 */
.form-header p {
  color: rgba(118, 64, 255, 0.84);
  font-size: 14px;
}

.floating-form .input-group {
  position: relative;
  margin-bottom: 25px;
}

/* 修改输入框宽度和边框颜色为紫色 */
.input-group input {
  width: 93%; /* 缩小输入框宽度 */
  padding: 12px;
  border: 1.5px solid rgba(118, 64, 255, 0.22); /* 设置边框颜色为紫色 */
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: transparent;
}

.input-group label {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: white;
  padding: 0 4px;
  color: #999999;
  font-size: 14px;
  transition: all 0.3s ease;
  pointer-events: none;
}

.input-group input:focus,
.input-group input:valid {
  border-color: rgba(165, 126, 250, 0.8);
}

.input-group input:focus + label,
.input-group input:valid + label {
  top: 0;
  font-size: 12px;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  margin-left: 0;
  background: linear-gradient(to right, rgb(165, 126, 250), rgba(170, 135, 246, 0.94));
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.submit-btn:hover {
  transform: translateY(-1.5px);
  box-shadow: 0 4px 12px rgba(138, 43, 226, 0.25);
}

.arrow-icon {
  border: solid white;
  border-width: 0 1.5px 1.5px 0;
  display: inline-block;
  padding: 2.5px;
  transform: rotate(-45deg);
}

.form-footer {
  text-align: center;
  margin-top: 18px;
  color: #666666;
  font-size: 13px;
}

/* 修改立即注册文字颜色为红色 */
.form-footer a {
  color: rgba(99, 64, 255, 0.8);
  text-decoration: none;
  margin-left: 4px;
  font-weight: 500;
}

.form-footer a:hover {
  text-decoration: underline;
}

.error-message {
  color: rgba(246, 30, 30, 0.85);
  font-size: 12px;
  text-align: center;
  margin-bottom: 18px;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .register-container {
    padding: 25px;
    margin: 10px;
    max-width: 100%;
  }

  .form-header h2 {
    font-size: 22px;
  }

  .form-header p {
    font-size: 13px;
  }

  .input-group input {
    padding: 10px;
    font-size: 13px;
  }

  .input-group label {
    font-size: 13px;
  }

  .submit-btn {
    padding: 10px;
    font-size: 15px;
  }

  .arrow-icon {
    border-width: 0 1.3px 1.3px 0;
    padding: 2px;
  }
}

@media (max-width: 320px) {
  .register-container {
    padding: 20px;
  }

  .form-header h2 {
    font-size: 20px;
  }

  .input-group {
    margin-bottom: 20px;
  }
}
</style>