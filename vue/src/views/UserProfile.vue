<template>
  <div>
    <div class="user-profile">
      <el-container>
        <!-- 个人信息 -->
        <el-header class="header">
          <div class="avatar-wrapper">
            <div class="avatar-container">
              <el-avatar :src="user.picture" size="80"></el-avatar>
            </div>
            <div class="user-info">
              <h2>{{ user.name }}</h2>
              <div class="user-details">
                <span class="user-id">ID: {{ user.id }}</span>
                <span class="divider">|</span>
                <span class="user-email">{{ user.email }}</span>
              </div>
            </div>
          </div>
          <el-col :span="4" class="user-options">
            <el-button
                type="success"
                @click="logout"
                class="logout-btn">
              登出
            </el-button>
            <el-button
                type="primary"
                plain
                @click="showEditDialog"
                class="edit-btn">
              修改头像
            </el-button>
          </el-col>
        </el-header>

        <el-container class="main-container">
          <!-- 侧边选择栏 -->
          <el-aside width="220px" class="sidebar">
            <el-scrollbar>
              <el-menu
                  :default-active="activeIndex"
                  @select="handleSelect"
                  class="side-menu">
                <el-menu-item index="cart" class="menu-item">
                  <el-icon><el-icon-shopping-cart /></el-icon>
                  <span>收藏</span>
                </el-menu-item>
                <el-menu-item index="browsingRecords" class="menu-item">
                  <el-icon><el-icon-eye /></el-icon>
                  <span>浏览记录</span>
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </el-aside>

          <!-- 商品展示 -->
          <el-main class="content">
            <div v-if="activeIndex === 'cart'" class="section">
              <h3 class="section-title">购物车</h3>
              <div class="table-container">
                <table class="data-table">
                  <thead>
                  <tr>
                    <th>商品名称</th>
                    <th>图片</th>
                    <th>价格</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="item in paginatedCartItems" :key="item.id">
                    <td>{{ item.name }}</td>
                    <td><img :src="item.image" alt="商品图片" class="product-image" /></td>
                    <td class="price">¥{{ item.price }}</td>
                    <td>
                      <el-button type="primary" style="background-color: #8951fa; border-color: #8951fa;" @click="buyProduct(item)" class="action-btn">购买</el-button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <el-pagination
                  @current-change="handleCartPageChange"
                  :current-page="cartCurrentPage"
                  :page-size="pageSize"
                  :total="cartItems.length"
                  layout="total, prev, pager, next, jumper"
                  :background="true"
                  class="pagination"
              ></el-pagination>
            </div>

            <div v-else-if="activeIndex === 'browsingRecords'" class="section">
              <h3 class="section-title">浏览记录</h3>
              <div class="table-container">
                <table class="data-table">
                  <thead>
                  <tr>
                    <th>商品名称</th>
                    <th>图片</th>
                    <th>价格</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="item in paginatedBrowsingRecords" :key="item.id">
                    <td>{{ item.name }}</td>
                    <td><img :src="item.image" alt="商品图片" class="product-image" /></td>
                    <td class="price">¥{{ item.price }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <el-pagination
                  @current-change="handleBrowsingPageChange"
                  :current-page="browsingCurrentPage"
                  :page-size="pageSize"
                  :total="browsingRecords.length"
                  layout="total, prev, pager, next, jumper"
                  :background="true"
                  class="pagination"
              ></el-pagination>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </div>

    <!-- 修改用户的对话框 -->
    <el-dialog
        title="修改头像"
        v-model="editDialogVisible"
        width="400px"
        @close="editDialogClosed"
        :close-on-click-modal="false"
        class="edit-dialog">
      <el-form :model="editUserForm" ref="editUserFormRef" label-width="80px" class="edit-form">
        <el-form-item label="上传头像" prop="picture">
          <el-upload
              :before-upload="handleBeforeUpload"
              :on-change="handleAvatarChange"
              :limit="1"
              :auto-upload="false"
              action="/api/upload"
              class="avatar-upload"
          >
            <el-button type="primary">选择图片</el-button>
          </el-upload>
          <div v-if="editUserForm.picture" class="avatar-preview">
            <el-avatar :src="editUserForm.picture" size="80"></el-avatar>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="editUser">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {computed, onMounted, ref} from 'vue';
import 'element-plus/dist/index.css';
import {
  getProductList,
  getWantListProduct,
  updateUser,
  upload
} from '@/api';
import router from "@/router";
import {ElMessage} from "element-plus"; // 导入API方法
import { v4 as uuidv4 } from 'uuid'; // 引入 uuid 库

export default {
  name: 'SidebarMenu',
  setup() {
    //修改部分
    const logout = () => {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('userRole');
      router.push({name: 'Login'});
    };

    const user = ref(JSON.parse(localStorage.getItem('user')) || {});
    const activeIndex = ref('cart');
    const cartItems = ref([]);
    const browsingRecords = ref([]);
    const editDialogVisible = ref(false);
    const pageSize = 5;
    const cartCurrentPage = ref(1);
    const browsingCurrentPage = ref(1);

    const getCartItems = () => {
      getWantListProduct(user.value.id)
          .then(response => {
            cartItems.value = response.data.data || [];
            // status = 1 only
            cartItems.value = cartItems.value.filter(item => item.status === 1);
          })
          .catch(error => {
            console.error('Failed to fetch cart items:', error);
          });
    }

    const getBrowsingRecords = () => {
      getProductList()
          .then(response => {
            // 这里需要根据实际情况筛选浏览记录
            browsingRecords.value = response.data.data || [];
          })
          .catch(error => {
            console.error('Failed to fetch browsing records:', error);
          });
    }

    const handleSelect = (key) => {
      activeIndex.value = key;
    };

    const showEditDialog = () => {
      editDialogVisible.value = true; // 显示对话框
    };

    const editUserForm = ref({
      picture: user.value.picture || '',
    });

    const handleBeforeUpload = (file) => {
      // 限制图片大小和格式
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJpgOrPng) {
        ElMessage.error('上传图片只能是 JPG 或 PNG 格式!');
        return false;
      }
      if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2MB!');
        return false;
      }
      return true;
    };

    const handleAvatarChange = (file) => {
      const formData = new FormData();
      formData.append('image', file.raw);

      // 调用后端 API 上传文件
      upload(formData)
          .then((response) => {
            if (response.data.code === 1) {
              editUserForm.value.picture = response.data.data; // 返回头像 URL
            } else {
              ElMessage.error(response.data.message || '头像上传失败');
            }
          })
          .catch((error) => {
            console.error('Error uploading avatar:', error);
            ElMessage.error('上传失败，请稍后重试');
          });
    };

    const editUser = () => {
      user.value.picture = editUserForm.value.picture;

      updateUser(user.value)
          .then((response) => {
            if (response.data.code === 1) {
              ElMessage.success('头像更新成功');
              localStorage.setItem('user', JSON.stringify(user.value));
              editDialogVisible.value = false; // 关闭对话框
            } else {
              ElMessage.error(response.data.message || '头像更新失败');
            }
          })
          .catch((error) => {
            console.error('Error updating user:', error);
            ElMessage.error('更新失败，请稍后重试');
          });
    };

    const buyProduct = (product) => {
      try {
        const subject = product.name;             // Product name
        const traceNo = product.id + '-' + uuidv4();               // Product ID (order trace number)
        const totalAmount = product.price;        // Product price
        const sellerId = product.sellerId;        // Seller ID
        const buyerId = JSON.parse(localStorage.getItem('user')).id; // Buyer ID
        // body = sellerId,buyerId
        const body = sellerId + ',' + buyerId;

        // Construct the payment URL with the necessary query parameters
        const paymentUrl = `http://127.0.0.1:8080/alipay/pay?subject=${encodeURIComponent(subject)}&traceNo=${traceNo}&totalAmount=${totalAmount}&body=${body}`;

        // window.open(paymentUrl, '_blank'); // Open the payment URL in a new tab
        window.open(paymentUrl, '_self'); // Open the payment URL in the same tab

        // Display a success message indicating the purchase has started
        ElMessage.success(`已开始购买: ${subject}`);
      } catch (error) {
        // Handle potential errors (like issues with API calls)
        console.error('Error during purchase: ', error);
        ElMessage.error('购买失败，请稍后再试');
      }
    };

    // 分页处理函数
    const handleCartPageChange = (page) => {
      cartCurrentPage.value = page;
    };
    const handleBrowsingPageChange = (page) => {
      browsingCurrentPage.value = page;
    };

    // 获取每一页的数据
    const paginatedCartItems = computed(() => {
      const start = (cartCurrentPage.value - 1) * pageSize;
      return cartItems.value.slice(start, start + pageSize);
    });

    const paginatedBrowsingRecords = computed(() => {
      const start = (browsingCurrentPage.value - 1) * pageSize;
      return browsingRecords.value.slice(start, start + pageSize);
    });

    onMounted(() => {
      getCartItems();
      getBrowsingRecords();
    });

    return {
      logout, //修改
      user,
      showEditDialog,
      handleSelect,
      activeIndex,
      cartItems,
      browsingRecords,
      cartCurrentPage,
      browsingCurrentPage,
      pageSize,
      handleCartPageChange,
      handleBrowsingPageChange,
      paginatedCartItems,
      paginatedBrowsingRecords,
      editDialogVisible,
      editUserForm,
      handleAvatarChange,
      editUser,
      handleBeforeUpload,
      buyProduct,
    };
  }
};
</script>

<style scoped>
.user-profile {
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* 顶部用户信息样式 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 40px;
  background-color: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: auto !important;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-container {
  padding: 4px;
  border-radius: 50%;
  background: linear-gradient(45deg, rgba(99, 64, 255, 0.3), #7a8ffb);
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-info h2 {
  margin: 0;
  font-size: 24px;
  color: #302c50;
  font-weight: 600;
}

.user-details {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #606266;
  font-size: 14px;
}

.divider {
  color: #dcdfe6;
}

.user-options {
  display: flex;
  gap: 12px;
}

.logout-btn {
  /* background-color: #4CAF50 !important;
  border-color: #4CAF50 !important; */
  background-color: #ffcccc !important;
  border-color: #ffcccc !important;
  color: grey !important;
}

.logout-btn:hover {
  background-color: #ff6666 !important;
  border-color: #ff6666 !important;
  color: white !important;
}

.edit-btn {
  background-color: #fff !important;
  border-color: rgba(165, 126, 250, 0.7) !important;
  color: grey !important;
}

.edit-btn:hover {
  background-color: rgb(165, 126, 250) !important;
  border-color: #a57efa !important;
  color: white !important;
}

/* 主容器样式 */
.main-container {
  margin: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 侧边栏样式 */
.sidebar {
  background-color: #ffffff;
  border-right: 1px solid #e6e6e6;
}

.side-menu {
  border-right: none;
}

.menu-item {
  height: 56px;
  line-height: 56px;
  margin: 8px 0;
}

.menu-item:hover {
  background-color: #f5f7fa;
}

/* 修改选中项文字颜色为紫色 */
.el-menu-item.is-active {
  color: #8951fa !important;
}

/* 内容区域样式 */
.content {
  padding: 24px;
}

.section-title {
  margin: 0 0 24px 0;
  font-size: 20px;
  color: #2c3e50;
  font-weight: 600;
}

.table-container {
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.data-table th {
  background-color: #f5f7fa;
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

.data-table td {
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
  color: #606266;
}

.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.price {
  color: #f56c6c;
  font-weight: 600;
}

.action-btn {
  padding: 8px 16px;
  font-size: 14px;
}

/* 分页样式 */
.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* 对话框样式 */
.edit-dialog {
  border-radius: 8px;
}

.edit-form {
  padding: 20px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.avatar-preview {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 16px;
  }

  .user-options {
    width: 100%;
    justify-content: center;
  }

  .main-container {
    margin: 10px;
  }

  .sidebar {
    width: 100% !important;
  }
}
</style>