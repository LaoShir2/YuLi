const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/**': {
        target: 'http://localhost:8080',  // 设置后端 Spring Boot 的 API 地址
        changeOrigin: true,               // 为虚拟主机做适配
        secure: false                     // 如果后端没有使用 https，设置为 false
      }
    }
  },
})

module.exports = {
  chainWebpack: config => {
    config.resolve.alias
      .set('@', require('path').resolve(__dirname, 'src'))
  }
}
