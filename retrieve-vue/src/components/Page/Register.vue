<template>
  <div class="card">
    <el-card shadow="always">
      <el-space
          direction="vertical"
          fill="fill"
          fill-ratio="90"
          size="large"
      >
        <!--    页标题-->
        <el-row>
          <h2 class="title">
            注册
          </h2>
        </el-row>
        <!--    账号输入框-->
        <el-row>
          <el-input placeholder="请输入用户名" size="large" v-model="registerUser.name">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-row>
        <!--    密码输入框-->
        <el-row>
          <el-input placeholder="请输入密码" size="large" type="password" v-model="registerUser.passwd">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-row>
        <!--    密码确认框-->
        <el-row>
          <el-input placeholder="请确认密码" size="large" type="password" v-model="confirm"
                    @keyup.enter.native="registerEvent">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-row>
        <!--    操作Button-->
        <el-row :gutter="30">
          <el-col :span="12" align="middle">
            <el-button size="large" type="success" @click="registerEvent">
              注册
            </el-button>
          </el-col>
          <el-col :span="12" align="middle">
            <el-button size="large" type="primary" @click="jumpToLogin">
              返回
            </el-button>
          </el-col>
        </el-row>
      </el-space>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Register"
}
</script>

<script setup>
import {reactive, ref} from "vue";

import jump from "@/routers/jump";
import UserService from "@/services/UserService";
import {ElMessage} from "element-plus";
import user from "@/items/user"
import {useStore} from "vuex";

const store = useStore()

const confirm = ref('')

const registerUser = reactive(
    new user(null, "", "")
)
const registerEvent = async function () {
  //如果两次输入的密码一致
  if (registerUser.passwd === confirm.value) {
    await UserService.addUser(registerUser).then(res => {
      if (res === 0) {
        ElMessage({
          message: '注册成功！即将跳转到登录页',
          type: "success"
        })
        store.dispatch("changeUserName", registerUser.name)
        jump.jumpToLogin()
      } else if (res === -1) {
        ElMessage({
          message: '注册失败，已存在相同用户名的用户',
        })
      }
    })
  } else {
    ElMessage({
      message: '两次输入的密码不一致',
    })
  }
}

const jumpToLogin = async function () {
  await jump.jumpToLogin()
}
</script>

<style scoped>
.title {
  font-size: 1.5em;
  font-weight: bold;
  margin: auto;
}

.card {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh
}
</style>