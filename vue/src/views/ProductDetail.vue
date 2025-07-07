<template>
  <div class="product-detail-container">
    <el-container>
      <!-- 左侧商品图片和详情展示 -->
      <el-main class="product-main-section">
        <el-card class="image-card">
          <img :src="product.image" alt="product image" class="main-product-image"/>
        </el-card>

        <div class="product-info-section">
          <h1 class="product-title">{{ product.name }}</h1>
          <div class="price-section">
            <span class="price-label">价格:</span>
            <span class="price-amount">¥{{ product.price }}</span>
          </div>

          <div class="description-section">
            <h3>商品描述</h3>
            <p class="description-text">{{ product.description }}</p>
          </div>

          <div class="action-buttons">
            <el-button type="primary" size="large" @click="buyProduct">立即购买</el-button>
            <el-button
                :type="this.product.isLiked? 'danger' : 'success'"
                size="large"
                @click="toggleFavorite"
            >
              <el-icon><Star /></el-icon>
              {{ this.product.isLiked? '取消收藏' : '收藏' }}
            </el-button>
            <el-button @click="goToHome" size="large">返回首页</el-button>
          </div>
        </div>
      </el-main>

      <!-- 右侧相关商品推荐 -->
      <el-aside width="25%" class="related-products-section">
        <div class="related-header">
          <h2>相关商品</h2>
        </div>
        <div class="related-products-list">
          <el-card v-for="item in relatedProducts"
                   :key="item.id"
                   class="related-product-card"
                   shadow="hover">
            <img :src="item.image" alt="商品图片" class="related-product-image"/>
            <div class="related-product-info">
              <h4>{{ item.name }}</h4>
              <p class="related-price">¥{{ item.price }}</p>
              <el-button type="primary"
                         size="small"
                         @click="viewProductDetail(item.id)">
                查看详情
              </el-button>
            </div>
          </el-card>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import {getProductDetail, getProductList, toggleProductWantList, getWantList} from '@/api';
import {ElMessage} from 'element-plus';
import {Star} from '@element-plus/icons-vue';
import { v4 as uuidv4 } from 'uuid';

export default {
  components: {
    Star
  },
  data() {
    return {
      product: {},
      relatedProducts: [],
      userWantList: [],
    };
  },
  methods: {
    fetchProductDetail() {
      const productId = this.$route.params.id;
      getProductDetail(productId)
          .then(response => {
            this.product = response.data.data;
            this.updateProductLikes();
            this.fetchRelatedProducts();
          })
          .catch(error => {
            console.error("Failed to fetch product details:", error);
            this.$message.error('Failed to fetch product details');
          });
    },
    fetchRelatedProducts() {
      getProductList()
          .then(response => {
            this.relatedProducts = response.data.data.filter(product =>
                product.id!== this.product.id &&
                product.status === 1
            ).slice(0, 3);
          })
          .catch(error => {
            console.error("Failed to fetch related products:", error);
            this.$message.error('Failed to fetch related products');
          });
    },
    fetchUserWantList() {
      const userId = JSON.parse(localStorage.getItem('user')).id;
      getWantList(userId)
          .then((response) => {
            if (response && response.data && response.data.code === 1) {
              this.userWantList = response.data.data;
              this.updateProductLikes();
            } else {
              this.$message.error('无法加载收藏列表');
            }
          })
          .catch((error) => {
            console.error('API 请求失败：', error);
            this.$message.error('加载收藏列表失败');
          });
    },
    updateProductLikes() {
      this.product.isLiked = this.userWantList.includes(this.product.id);
    },
    toggleFavorite() {
      const userId = JSON.parse(localStorage.getItem('user')).id;
      this.product.isLiked = !this.product.isLiked;

      toggleProductWantList(userId, this.product.id)
          .then((response) => {
            if (response && response.data && response.data.code === 1) {
              const message = this.product.isLiked ? '已收藏' : '已取消收藏';
              ElMessage.success(message);
            } else {
              ElMessage.error(response.data.message || '操作失败');
            }
          })
          .catch((error) => {
            console.error('API 请求失败：', error);
            ElMessage.error('操作失败，请稍后重试');
          });
    },
    viewProductDetail(productId) {
      this.$router.push('/products/' + productId);
    },
    goToHome() {
      this.$router.push('/home');
    },
    async buyProduct() {
      try {
        const subject = this.product.name;
        const traceNo = this.product.id + '-' + uuidv4();
        const totalAmount = this.product.price;
        const sellerId = this.product.sellerId;
        const buyerId = JSON.parse(localStorage.getItem('user')).id;
        const body = sellerId + ',' + buyerId;

        const paymentUrl = `http://127.0.0.1:8080/alipay/pay?subject=${encodeURIComponent(subject)}&traceNo=${traceNo}&totalAmount=${totalAmount}&body=${body}`;
        window.open(paymentUrl, '_self');
        ElMessage.success(`已开始购买: ${subject}`);
      } catch (error) {
        console.error('Error during purchase: ', error);
        ElMessage.error('购买失败，请稍后再试');
      }
    },
  },
  watch: {
    '$route'() {
      this.fetchProductDetail();
      this.fetchUserWantList();
    }
  },
  created() {
    this.fetchProductDetail();
    this.fetchUserWantList();
  },
};
</script>

<style scoped>
.product-detail-container {
  padding: 10px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.product-main-section {
  padding: 20px;
  margin-right: 20px;
}

.image-card {
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.main-product-image {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.product-info-section {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.product-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #303133;
}

.price-section {
  margin: 20px 0;
  padding: 15px;
  background: #f8f8f8;
  border-radius: 4px;
}

.price-label {
  font-size: 16px;
  color: #606266;
}

.price-amount {
  font-size: 28px;
  color: #f56c6c;
  margin-left: 10px;
  font-weight: bold;
}

.description-section {
  margin: 30px 0;
  padding: 20px;
  background: #f8f8f8;
  border-radius: 4px;
}

.description-text {
  line-height: 1.6;
  color: #606266;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  gap: 15px;
  justify-content: center;
}

.related-products-section {
  margin-top: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.related-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.related-product-card {
  margin-bottom: 15px;
  transition: transform 0.3s;
}

.related-product-card:hover {
  transform: translateY(-5px);
}

.related-product-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 4px;
}

.related-product-info {
  padding: 10px;
  text-align: center;
}

.related-price {
  color: #f56c6c;
  font-weight: bold;
  margin: 10px 0;
}
</style>