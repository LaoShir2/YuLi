<template>
  <div class="form-container">
    <h2 class="center-title">发布闲置</h2>
    <el-form :model="product" ref="form" label-width="120px" class="publish-form">
      <!-- Product Name Input -->
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="product.name" placeholder="请输入商品名称" class="short-input"></el-input>
      </el-form-item>

      <!-- Price Input -->
      <el-form-item label="价格" prop="price">
        <el-input v-model="product.price" placeholder="请输入商品价格" class="short-input">
          <template #prefix>¥</template>
        </el-input>
      </el-form-item>

      <!-- Location Input -->
      <el-form-item label="地址" prop="location">
        <el-select v-model="product.address" placeholder="请选择校区" class="short-input">
          <el-option v-for="province in provinces"
                     :key="province.code"
                     :label="province.name"
                     :value="province.code">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- Image Upload -->
      <el-form-item label="图片" prop="image">
        <el-upload
            ref="upload"
            class="upload-demo"
            :limit="1"
            :on-change="handleFileChange"
            :auto-upload="false"
            action="/upload"
            drag
        >
          <template #trigger>
            <div class="upload-area">
              <el-icon class="upload-icon"><Upload /></el-icon>
              <div class="upload-text">点击或拖拽图片上传</div>
              <div class="upload-tip">支持 jpg、png 格式</div>
            </div>
          </template>
        </el-upload>
        <div v-if="product.image" class="image-preview">
          <img :src="product.image" alt="商品预览图" class="uploaded-image" />
        </div>
      </el-form-item>

      <!-- Condition Input -->
      <el-form-item label="新旧程度" prop="condition">
        <el-select v-model="product.condition" placeholder="请选择新旧程度" class="short-input">
          <el-option v-for="level in levels"
                     :key="level.code"
                     :label="level.name"
                     :value="level.code">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- Description Input -->
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" 
                  v-model="product.description" 
                  placeholder="请详细描述商品的具体情况，例如：使用时长、磨损程度等"
                  :rows="4"
                  class="description-input">
        </el-input>
      </el-form-item>

      <!-- Publish Button -->
      <el-form-item>
        <el-button type="primary" @click="submitProduct" class="submit-button">发布商品</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { publishProduct, upload } from '@/api';
import { Upload } from '@element-plus/icons-vue';

export default {
  components: {
    Upload
  },
  data() {
    return {
      product: {
        image: "",
        description: "",
        name: "",
        price: "",
        address: "",
        buyerId: "",
        sellerId: 1,
        condition:"",
        status: 1,
        createTime: "",
        likes: 0,
      },
      provinces: [
        { code: '1', name: '广州南校区' },
        { code: '2', name: '广州东校区' },
        { code: '3', name: '广州北校区' },
        { code: '4', name: '深圳校区' },
        { code: '5', name: '珠海校区' }
      ],
      levels: [
        { code: '1', name: '二手' },
        { code: '2', name: '全新' },
      ],
    };
  },
  methods: {
    handleFileChange(file) {
      const formData = new FormData();
      formData.append('image', file.raw);

      upload(formData)
          .then((result) => {
            if (result.data.code === 1) {
              this.product.image = result.data.data;
              this.$message({
                message: '图片上传成功!',
                type: 'success',
              });
              this.$refs.upload.clearFiles();
            } else {
              this.$message({
                message: '图片上传失败',
                type: 'error',
              });
            }
          })
          .catch((error) => {
            console.error('上传图片出错:', error);
            this.$message({
              message: '上传图片时发生错误',
              type: 'error',
            });
          });
    },

    submitProduct() {
      if (!this.product.name || !this.product.price || !this.product.description || !this.product.image) {
        this.$message.error('请填写所有必填项并上传图片');
        return;
      }

      this.product.sellerId = JSON.parse(localStorage.getItem('user')).id;

      publishProduct(this.product)
          .then((result) => {
            if (result.data.code === 1) {
              this.$message({
                message: '商品发布成功!',
                type: 'success',
              });
              this.$router.push('/home');
            } else {
              this.$message({
                message: '商品发布失败',
                type: 'error',
              });
            }
          })
          .catch((error) => {
            console.error('发布商品出错:', error);
            this.$message({
              message: '发布商品时发生错误',
              type: 'error',
            });
          });
    },
  },
};
</script>

<style scoped>

.form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  background: linear-gradient(to bottom, #E6F3FF, #F5F9FC);
  height: auto;
}

.publish-form {
  width: 100%;
  max-width: 800px;
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.center-title {
  color: #303133;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
  text-align: center;
}

.short-input {
  width: 300px;
  transition: all 0.3s ease;
  border: 1px solid #dcdfe6;
}

.short-input:hover {
  border-color: #409EFF;
  box-shadow: 0 0 8px rgba(0,0,0,0.1);
}

.description-input {
  width: 100%;
}

.upload-area {
  padding: 5px;
  text-align: center;
  border: dashed #dcdfe6;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: #409EFF;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 10px;
}

.upload-text {
  color: #606266;
  font-size: 14px;
  margin-bottom: 5px;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
}

.image-preview {
  margin-top: 20px;
  text-align: center;
}

.uploaded-image {
  width: 500px;
  height: auto;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.uploaded-image:hover {
  transform: scale(1.02);
}

.submit-button {
  width: 200px;
  height: 40px;
  font-size: 16px;
  display: block;
  margin: 30px auto 0;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.submit-button:hover {
  background-color: #2679f5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64,158,255,0.4);
}
</style>