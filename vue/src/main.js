import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import axios from 'axios';
import store from './store';

const app = createApp(App);

app.config.globalProperties.$axios = axios;
app.use(router);
app.use(ElementPlus);
app.use(store);

app.mount('#app');
