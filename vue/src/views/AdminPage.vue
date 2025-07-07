<template>
  <div class="admin-container">
    <!-- Header -->
    <div class="header">
      <div class="logo">管理</div>
      <div class="user-info">
        <button @click="logout">登出</button>
      </div>
    </div>

    <div class="main-content">
      <!-- Sidebar -->
      <div class="sidebar">
        <ul>
          <li @click="navigateTo('product')" :class="{ active: currentTab === 'product' }">
            <el-icon><GoodsFilled /></el-icon>
            商品管理
          </li>
          <li @click="navigateTo('user')" :class="{ active: currentTab === 'user' }">
            <el-icon><UserFilled /></el-icon>
            用户管理
          </li>
          <li @click="navigateTo('transaction')" :class="{ active: currentTab === 'transaction' }">
            <el-icon><List /></el-icon>
            交易记录
          </li>
          <li @click="navigateTo('announcement')" :class="{ active: currentTab === 'announcement' }">
            <el-icon><Promotion /></el-icon>
            公告发布
          </li>
        </ul>
      </div>

      <!-- Main Content -->
      <div class="content">
        <div v-if="currentTab === 'product'">
          <h3>商品管理</h3>
          <table>
            <thead>
            <tr>
              <th>商品名称</th>
              <th>图片</th>
              <th>价格</th>
              <th>地址</th>
              <th>创建时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="product in pagedProducts" :key="product.id">
              <td>{{ product.name }}</td>
              <td><img :src="product.image" alt="商品图片" class="image"/></td>
              <td>{{ product.price }}</td>
              <td>{{ product.address }}</td>
              <td>{{ product.createTime }}</td>
              <td>{{ product.status === 1 ? '在售' : '已售出' }}</td>
              <td>
                <el-button type="primary" @click="deleteProduct(product.id)">删除</el-button>
              </td>
            </tr>
            </tbody>
          </table>

          <br>

          <!-- Pagination for products -->
          <el-pagination
              background
              layout="prev, pager, next"
              :total="products.length"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />
        </div>

        <div v-if="currentTab === 'user'">
          <h3>用户管理</h3>
          <table>
            <thead>
            <tr>
              <th>用户id</th>
              <th>用户名</th>
              <th>邮箱</th>
              <th>头像</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in pagedUsers" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.name }}</td>
              <td>{{ user.email }}</td>
              <td><img :src="user.picture" alt="头像" class="image"/></td>
              <td>
                <el-button type="primary" @click="deleteUser(user.id)">删除</el-button>
              </td>
            </tr>
            </tbody>
          </table>

          <br>

          <!-- Pagination for users -->
          <el-pagination
              background
              layout="prev, pager, next"
              :total="users.length"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />
        </div>

        <div v-if="currentTab === 'transaction'">
          <h3>交易记录</h3>
          <table>
            <thead>
            <tr>
              <th>交易ID</th>
              <th>卖家ID</th>
              <th>买家ID</th>
              <th>商品ID</th>
              <th>商品名称</th>
              <th>商品图片</th>
              <th>交易金额</th>
              <th>交易时间</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="trade in pagedTrades" :key="trade.id">
              <td>{{ trade.id }}</td>
              <td>{{ trade.sellerId }}</td>
              <td>{{ trade.buyerId }}</td>
              <td>{{ trade.productId }}</td>
              <td>{{ trade.productName }}</td>
              <td><img :src="products.find(product => product.id === trade.productId).image" alt="商品图片"
                       class="image"/></td>
              <td>¥ {{ trade.totalAmount }}</td>
              <td>{{ trade.createTime }}</td>
            </tr>
            </tbody>
          </table>

          <br>

          <!-- Pagination for trades -->
          <el-pagination
              background
              layout="prev, pager, next"
              :total="trades.length"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />
        </div>

        <!-- Announcement Management Tab -->
        <div v-if="currentTab === 'announcement'">
          <h3>公告发布</h3>
          <form @submit.prevent="submitAnnouncement">
            <div>
              <label for="title">公告标题</label>
              <input type="text" id="title" v-model="announcement.title" required/>
            </div>
            <div>
              <label for="content">公告内容</label>
              <textarea id="content" v-model="announcement.content" rows="6" required></textarea>
            </div>
            <button type="submit" @click="addNewAnnouncement">发布公告</button>
          </form>
          <h3>历史公告</h3>
          <table>
            <thead>
            <tr>
              <th>公告标题</th>
              <th>公告内容</th>
              <th>发布时间</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="announcement in pagedAnnouncements" :key="announcement.id">
              <td>{{ announcement.title }}</td>
              <td>{{ announcement.content }}</td>
              <td>{{ announcement.createTime }}</td>
              <td>
                <el-button type="primary" @click="deleteAnnouncement(announcement.id)">删除</el-button>
              </td>
            </tr>
            </tbody>
          </table>

          <br>
          <!-- Pagination for trades -->
          <el-pagination
              background
              layout="prev, pager, next"
              :total="announcements.length"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {ElMessage, ElPagination} from 'element-plus';
import {
  getProductList,
  deleteProductById,
  deleteUserById,
  getUserList,
  getUserById,
  getTradeList,
  addAnnouncement,
  getAnnouncementList, deleteAnnouncementById
} from '@/api';
import {GoodsFilled, UserFilled, List, Promotion} from '@element-plus/icons-vue';

export default {
  name: 'AdminPage',
  components: {
    ElPagination,
    GoodsFilled,
    UserFilled,
    List,
    Promotion,
  },
  data() {
    return {
      currentTab: 'product', // 当前选中的 tab
      pageSize: 5, // 每页显示的商品数/用户数/交易记录数
      currentPage: 1, // 当前页数
      products: [], // 商品数据
      users: [], // 用户数据
      trades: [], // 交易数据
      announcements: [], // 公告数据
      announcement: {title: '', content: ''}, // 当前发布的公告数据
    };
  },
  computed: {
    // 分页后的商品数据
    pagedProducts() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.products.slice(start, end);
    },
    // 分页后的用户数据
    pagedUsers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.users.slice(start, end);
    },
    // 分页后的交易记录数据
    pagedTrades() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.trades.slice(start, end);
    },
    pagedAnnouncements() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.announcements.slice(start, end);
    },
  },
  methods: {
    getAllProducts() {
      getProductList().then(response => {
        this.products = response.data.data;
      });
    },
    getAllUsers() {
      getUserList().then(response => {
        this.users = response.data.data;
      });
    },
    getAllTrades() {
      getTradeList().then(response => {
        this.trades = response.data.data;
      });
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    logout() {
      localStorage.removeItem('token'); // Remove the token from local storage
      localStorage.removeItem('user'); // Remove the user object from local storage
      localStorage.removeItem('userRole'); // Remove the user role from local storage
      this.$router.push({name: 'Login'}); // Navigate to the Login page
    },
    navigateTo(tab) {
      this.currentTab = tab;
    },
    deleteProduct(id) {
      deleteProductById(id).then(() => {
        ElMessage.success(`商品已删除`);
        // 刷新页面
        location.reload();
      });
    },
    deleteUser(id) {
      getUserById(id).then(response => {
        if (response.data.data.name === 'admin') {
          ElMessage.error('不能删除管理员');
        } else {
          deleteUserById(id).then(() => {
            ElMessage.success(`用户已删除`);
            // 刷新页面
            location.reload();
          });
        }
      });
    },
    submitAnnouncement() {
      if (this.announcement.title && this.announcement.content) {
        // Add the current date and time to the announcement
        const currentTime = new Date().toLocaleString();
        this.announcements.push({...this.announcement, createdAt: currentTime});
        this.announcement.title = '';
        this.announcement.content = '';
        ElMessage.success('公告发布成功');
        location.reload();
      }
    },
    getAllAnnouncements() {
      getAnnouncementList().then(response => {
        this.announcements = response.data.data;
      });
    },
    addNewAnnouncement() {
      addAnnouncement(this.announcement).then(() => {
        this.announcement.title = '';
        this.announcement.content = '';
      });
    },
    deleteAnnouncement(id) {
      // Delete the announcement by id
      deleteAnnouncementById(id).then(() => {
        ElMessage.success('公告已删除');
        // Refresh the page
        location.reload();
      });
    },
  },
  mounted() {
    this.getAllProducts();
    this.getAllUsers();
    this.getAllTrades();
    this.getAllAnnouncements();
  },
};
</script>

<style scoped>
.admin-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  font-family: 'Arial', sans-serif;
  background: linear-gradient(to bottom, #F0F8FF, #E6F3FF);
}

.header {
  display: flex;
  justify-content: space-between;
  padding: 15px 30px;
  background-color: lightskyblue;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header .logo {
  font-size: 30px;
  font-weight: bold;
}

.header .user-info button {
  background: #007BFF;
  color: white;
  border: none;
  padding: 8px 18px;
  cursor: pointer;
  font-size: 14px;
  border-radius: 4px;
  transition: background 0.3s;
}

.header .user-info button:hover {
  background: #c0392b;
}

.main-content {
  display: flex;
  flex-grow: 1;
  background-color: #ffffff;
  overflow: hidden; /* 确保内容不溢出 */
}

.sidebar {
  width: 200px;
  flex-shrink: 0; /* 防止被压缩 */
  background-color: lightskyblue;
  color: white;
  padding-top: 30px;
  box-shadow: 2px 0 5px rgb(51.2, 126.4, 204);
}

.sidebar ul {
  list-style: none;
  padding-left: 0;
}

.sidebar li {
  padding: 12px;
  cursor: pointer;
  text-align: center;
  font-size: 17px;
  transition: background-color 0.3s, color 0.3s;
}

.sidebar li:hover,
.sidebar li.active {
  background-color: #2980b9;
  color: white;
}

.content {
  flex-grow: 1;
  padding: 30px;
  overflow-y: auto;
  overflow-x: hidden; /* 避免出现水平滚动条 */
}

h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 15px;
  line-height: 1.8;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  table-layout: fixed; /* 固定表格布局，防止列宽自动调整 */
}

table th, table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
  word-wrap: break-word; /* 处理长文本换行 */
}

table th {
  background-color: #ecf0f1;
  color: #34495e;
  white-space: nowrap; /* 避免表头文字换行 */
}

table td {
  white-space: normal; /* 允许单元格内容换行 */
}

button {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  font-size: 14px;
}

button:hover {
  background-color: #d5d5d5;
}

form div {
  margin-bottom: 15px;
}

input, textarea {
  width: 100%;
  padding: 8px;
}

button[type="submit"] {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button[type="submit"]:hover {
  background-color: #45a049;
}

.image {
  max-width: 100px;
  height: auto;
}
</style>
