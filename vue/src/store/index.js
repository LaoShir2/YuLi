import {createStore} from "vuex";

export default createStore({
    state: {
        token: localStorage.getItem("token"),
        user: JSON.parse(localStorage.getItem("user")),
        userRole: localStorage.getItem("userRole")
    },
    mutations: {
    },
    getters: {
    },
    actions: {
    },
    modules: {}
});
