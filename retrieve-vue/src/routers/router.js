import {createRouter, createWebHistory} from "vue-router";

import Register from "@/components/Page/Register.vue";
import Main from "@/components/Page/Main.vue";
import store from "@/store";
import DocManager from "@/components/Page/DocManager.vue";
import SecondSearch from "@/components/Page/SecondSearch.vue";
import Collection from "@/components/Page/Collection.vue";
import BrowsingHistory from "@/components/Table/BrowsingHistory.vue";


const routes = [
    {
        path: "/login",
        name: "Login",
        component: () => import("@/components/Page/Login.vue"),
    },
    {
        path: "/register",
        name: "Register",
        component: Register,
    },
    {
        path: "/",
        name: "Main",
        component: Main,
    },
    {
        path: "/document",
        name: "Document",
        component: DocManager,
    },
    {
        path: "/detail",
        name: "SecondSearch",
        component: SecondSearch
    },
    {
        path: "/collect",
        name: "Collection",
        component: Collection
    },

    {
        path: "/history",
        name: "History",
        component: BrowsingHistory
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
});


router.beforeEach(async (to, from) => {
    console.log(store.state.LoginUser)
    if (
        // 检查用户是否已登录
        store.state.LoginUser.status === false &&
        // 避免无限重定向
        to.name !== 'Login' && to.name !== 'Register'
    ) {
        // 将用户重定向到登录页面
        return {name: 'Login'}
    }
})

export default router;