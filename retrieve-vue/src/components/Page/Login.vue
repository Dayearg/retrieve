<template>
  <el-row v-if="!isMobile()">
    <el-col :span="15">
      <el-carousel height="100vh" direction="vertical" :autoplay="true"
                   style="background-image: linear-gradient(to right , #FFFFFF, #97CEC4);">
        <el-carousel-item v-for="(item,index) in images" :key="index">
          <img :src="item.src" alt="image"/>
        </el-carousel-item>

      </el-carousel>
    </el-col>
    <el-col :span="9">
      <div class="card" style="background-image: linear-gradient(to right , #97CEC4, #69B6F0);">
        <el-card shadow="always"
                 style="border-radius: 20px; width:350px; height:350px; background-color:  rgba(255, 255, 255, 0.5)">
          <el-space
              direction="vertical"
              size="large"
              style="margin-left:30px"
          >
            <!--    页标题-->
            <el-row>
              <h2 class="title">
                登录
              </h2>
            </el-row>
            <!--    账号输入框-->
            <el-row>
              <el-input placeholder="请输入用户名" size="large" v-model="loginUser.name" style="width:250px">
                <template #prefix>
                  <el-icon>
                    <User/>
                  </el-icon>
                </template>
              </el-input>
            </el-row>
            <!--    密码输入框-->
            <el-row>
              <el-input placeholder="请输入密码" size="large" type="password" v-model="loginUser.passwd"
                        @keyup.enter.native="loginEvent" style="width:250px">
                <template #prefix>
                  <el-icon>
                    <Lock/>
                  </el-icon>
                </template>
              </el-input>
            </el-row>
            <!--    操作Button-->
            <el-row gutter="30" style="margin-top:20px">
              <el-col :span="12" align="middle">
                <el-button size="large" type="success" @click="loginEvent">
                  登录
                </el-button>
              </el-col>
              <el-col :span="12" align="middle">
                <el-button size="large" type="primary" @click="jumpToRegister">
                  注册
                </el-button>
              </el-col>
            </el-row>
          </el-space>
        </el-card>
      </div>
    </el-col>
  </el-row>

  <div class="card" v-if="isMobile()">
    <el-card shadow="always">
      <el-space
          direction="vertical"
          size="large"
      >
        <!--    页标题-->
        <el-row>
          <h2 class="title">
            登录
          </h2>
        </el-row>
        <!--    账号输入框-->
        <el-row>
          <el-input placeholder="请输入用户名" size="large" v-model="loginUser.name">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-row>
        <!--    密码输入框-->
        <el-row>
          <el-input placeholder="请输入密码" size="large" type="password" v-model="loginUser.passwd"
                    @keyup.enter.native="loginEvent">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-row>
        <!--    操作Button-->
        <el-row gutter="30">

          <el-col :span="12" align="left">
            <el-button size="large" type="success" @click="loginEvent">
              登录
            </el-button>
          </el-col>
          <el-col :span="12" align="middle">
            <el-button size="large" type="primary" @click="jumpToRegister">
              注册
            </el-button>
          </el-col>
        </el-row>
      </el-space>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Login"
}
</script>


<script setup>
import user from "@/items/user";
import UserService from "@/services/UserService";
import {ElMessage} from "element-plus";
import jump from "@/routers/jump";
import {onMounted, reactive, ref, watch} from "vue";
import {useStore} from "vuex";

const loginUser = reactive(new user(null, "", ""))
const store = useStore()
//登录事件
const loginEvent = async function () {
  await UserService.getUser(loginUser).then(res => {
    if (res === 0) {
      ElMessage({
        message: '登录成功！正在跳转到主页！',
        type: 'success',
      })
      store.dispatch("changeUserName", loginUser.name)
      store.dispatch("changeLoginStatus", true)
      jump.jumpToMain()
    } else if (res === -1) {
      ElMessage({
        message: '登录失败！用户不存在！',
      })
    } else if (res === -2) {
      ElMessage({
        message: '登录失败！用户或密码错误！',
      })
    }
  })
}

const jumpToRegister = async function () {
  await jump.jumpToRegister()
}

const isMobile = () => {
  return (window.screen.availWidth < 768)
}

onMounted(
    () => {
      loginUser.name = store.state.LoginUser.name
    }
)

const images = ref([
  {src: "webIcon/new1.png"},
  {src: "webIcon/newre2.png"},
  {src: "webIcon/newrere3.png"},
])

</script>

<style scoped>
.title {
  font-size: 1.8em;
  font-weight: bold;
  margin-left: auto;

}

.card {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh

}
</style>