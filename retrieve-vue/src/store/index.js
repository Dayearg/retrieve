import {createStore} from "vuex";


const store = createStore({
    state() {
        return {
            //判断已登录用户
            LoginUser: {
                name: "",
                status: false
            },
        }
    },
    mutations: {
        userStatus(state, status) {
            state.LoginUser.status = status
        },
        userName(state, name) {
            state.LoginUser.name = name
        },
    },
    actions: {
        changeLoginStatus({commit}, status) {
            commit('userStatus', status)
        },
        changeUserName({commit}, name) {
            commit('userName', name)
        },
    },
})

export default store


