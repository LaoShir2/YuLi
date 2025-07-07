import axios from 'axios';

// backend API 地址
axios.defaults.baseURL = 'http://localhost:8080';

// 添加请求拦截器，自动带上 token
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// 登录
export const login = (loginData) => {
    return axios.post('/login', loginData);
};

// 上传图片
export const upload = (image) => {
    return axios.post('/upload', image);
}


// 商品API
// 获取所有商品
export const getProductList = () => {
    return axios.get('/products');
}
// 根据卖家id获取商品
export const getPublishedProductBySellerId = (sellerId) => {
    return axios.get(`/products/profile/${sellerId}`);
}
// 根据wantList获取商品
export const getWantListProduct = (userId) => {
    return axios.get(`/products/wantList/${userId}`);
}
// 搜索商品
export const searchProduct = (keyword) => {
    return axios.get(`/products/search?keyword=${keyword}`);
}
// 发布商品
export const publishProduct = (product) => {
    return axios.post('/products', product);
};
// 获取商品详情
export const getProductDetail = (id) => {
    return axios.get(`/products/${id}`);
};
// 删除商品
export const deleteProductById = (id) => {
    return axios.delete(`/products/${id}`);
}
//按价格对商品排序低到高
export const getProductsListByPriceFromL = () => {
    return axios.get('/products/Lower');
}
//按价格对商品排序高到低
export const getProductsListByPriceFromH = () => {
    return axios.get('/products/Higher');
}
//按时间展示商品
export const getProductsListByTime = () => {
    return axios.get('/products/ByTime');
}
export const getProductsListByTimeWeek = () => {
    return axios.get('/products/ByTimeWeek');
}
export const getProductsListByTimeMonth = () => {
    return axios.get('/products/ByTimeMonth');
}
// 更新商品
export const updateProduct = (product) => {
    return axios.put(`/products`, product);
};


// 用户API
// 添加用户
export const addUser = (user) => {
    return axios.post('/users', user);
}
// 根据id获取用户
export const getUserById = (id) => {
    return axios.get(`/users/${id}`);
}
// 根据id获取卖家
export const getSellerById = (id) => {
    return axios.get(`/sellers/${id}`);
}
// 根据id删除user
export const deleteUserById = (id) => {
    return axios.delete(`/users/${id}`);
}
// 获取所有用户
export const getUserList = () => {
    return axios.get('/users');
}
// 将商品加入购物车
export const toggleProductWantList = (userId, productId) => {
    return axios.put('/users/wantList', null, {
        params: {
            userId: userId,  // Ensure userId is passed correctly
            productId: productId  // Ensure productId is passed correctly
        }
    });
}
// 获取wantList
export const getWantList = (userId) => {
    return axios.get(`/users/wantList/${userId}`);
}
// 更新用户信息
export const updateUser = (user) => {
    return axios.put('/users', user);
}


// trade api
// 获取所有交易
export const getTradeList = () => {
    return axios.get('/trades');
}
// 根据productId获取交易
export const getTradeByProductId = (productId) => {
    return axios.get(`/trades/${productId}`);
}
// 根据productId更新交易
export const updateTradeByProductId = (productId) => {
    return axios.put(`/trades/${productId}`);
}

// announcement api
// 获取所有公告
export const getAnnouncementList = () => {
    return axios.get('/announcements');
}

// 根据id获取公告
export const getAnnouncementById = (id) => {
    return axios.get(`/announcements/${id}`);
}

// 添加公告
export const addAnnouncement = (announcement) => {
    return axios.post('/announcements', announcement);
}

// 删除公告
export const deleteAnnouncementById = (id) => {
    return axios.delete(`/announcements/${id}`);
}

