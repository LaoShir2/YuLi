import axios from 'axios';

// backend API 
axios.defaults.baseURL = 'http://localhost:8080';


axios.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    console.error('请求出错：', error);
    // 可根据不同错误状态码给用户提示
    if (error.response) {
        switch (error.response.status) {
            case 401:
                console.log('未授权，请登录');
                // 跳转到登录页面
                router.push({ name: 'Login' });
                break;
            case 404:
                console.log('请求的资源不存在');
                break;
            case 500:
                console.log('服务器内部错误');
                break;
            default:
                console.log('未知错误');
        }
    }
    return Promise.reject(error);
});


export const login = (loginData) => {
    return axios.post('/login', loginData);
};


export const upload = (image) => {
    return axios.post('/upload', image);
}



export const getProductList = () => {
    return axios.get('/products');
}

export const getPublishedProductBySellerId = (sellerId) => {
    return axios.get(`/products/profile/${sellerId}`);
}

export const getWantListProduct = (userId) => {
    return axios.get(`/products/wantList/${userId}`);
}

export const searchProduct = (keyword) => {
    return axios.get(`/products/search?keyword=${keyword}`);
}

export const publishProduct = (product) => {
    return axios.post('/products', product);
};

export const getProductDetail = (id) => {
    return axios.get(`/products/${id}`);
};

export const deleteProductById = (id) => {
    return axios.delete(`/products/${id}`);
}

export const getProductsListByPriceFromL = () => {
    return axios.get('/products/Lower');
}

export const getProductsListByPriceFromH = () => {
    return axios.get('/products/Higher');
}

export const getProductsListByTime = () => {
    return axios.get('/products/ByTime');
}
export const getProductsListByTimeWeek = () => {
    return axios.get('/products/ByTimeWeek');
}
export const getProductsListByTimeMonth = () => {
    return axios.get('/products/ByTimeMonth');
}

export const updateProduct = (product) => {
    return axios.put(`/products`, product);
};



export const addUser = (user) => {
    return axios.post('/users', user);
}

export const getUserById = (id) => {
    return axios.get(`/users/${id}`);
}

export const getSellerById = (id) => {
    return axios.get(`/sellers/${id}`);
}

export const deleteUserById = (id) => {
    return axios.delete(`/users/${id}`);
}

export const getUserList = () => {
    return axios.get('/users');
}

export const toggleProductWantList = (userId, productId) => {
    return axios.put('/users/wantList', null, {
        params: {
            userId: userId,  // Ensure userId is passed correctly
            productId: productId  // Ensure productId is passed correctly
        }
    });
}

export const getWantList = (userId) => {
    return axios.get(`/users/wantList/${userId}`);
}

export const updateUser = (user) => {
    return axios.put('/users', user);
}



export const getTradeList = () => {
    return axios.get('/trades');
}

export const getTradeByProductId = (productId) => {
    return axios.get(`/trades/${productId}`);
}

export const updateTradeByProductId = (productId) => {
    return axios.put(`/trades/${productId}`);
}

// announcement api

export const getAnnouncementList = () => {
    return axios.get('/announcements');
}


export const getAnnouncementById = (id) => {
    return axios.get(`/announcements/${id}`);
}


export const addAnnouncement = (announcement) => {
    return axios.post('/announcements', announcement);
}


export const deleteAnnouncementById = (id) => {
    return axios.delete(`/announcements/${id}`);
}

