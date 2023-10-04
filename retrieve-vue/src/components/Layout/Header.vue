<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
      @select="handleSelect"
      @close="clickIndex"
      menu-trigger="click"
      style="background-image: linear-gradient(to bottom , #d9ecff, #ecf5ff);"
  >

    <el-menu-item index="0" v-if="!isMobile" style="font-size: 25px"><img src="../../../public/webIcon/headlogorere.png"
                                                                          style="height: 50px"/></el-menu-item>
    <el-menu-item index="0" @click="drawerControl" v-if="isMobile">
      <el-icon style="height: 60px ;width: 30px">
        <Expand/>
      </el-icon>
    </el-menu-item>
    <div class="flex-grow"/>

    <el-menu-item index="1" @click="jumpToMain">搜索</el-menu-item>
    <el-sub-menu index="2">
      <template #title>{{ name }}</template>
      <el-menu-item index="2-2" @click="jumpToDocument">
        <el-icon>
          <Document/>
        </el-icon>
        文档管理
      </el-menu-item>
      <el-menu-item index="2-4" @click="jumpToBrowsing">
        <el-icon>
          <Calendar />
        </el-icon>
        历史记录
      </el-menu-item>
      <el-menu-item index="2-1" @click="jumpToCollect">
        <el-icon>
          <Star/>
        </el-icon>
        收藏
      </el-menu-item>
      <el-menu-item index="2-3" @click="loginOut">
        <el-icon>
          <SwitchButton/>
        </el-icon>
        注销登录
      </el-menu-item>
    </el-sub-menu>
  </el-menu>

  <el-drawer v-model="draw"
             size="80%"
             :with-header="false"
             direction="ltr">

    <p>
      <el-calendar class="custom-calendar" v-if="$route.name==='History'" v-model="globalVar.Calendar.value" >
        <template #header="{ date }">
          <span>{{ date }}</span>
          <el-button-group>
          </el-button-group>
        </template>
      </el-calendar>
      <HistoryTable></HistoryTable>
    </p>
    <SecondDistribution
        v-if="$route.name==='SecondSearch' && globalVar.SecondDocuments.value.length!==0"></SecondDistribution>
    <SearchDistribution v-if="globalVar.FirstDocuments.value.length!==0"></SearchDistribution>
    <RecentFile style="height: 300px" v-if="globalVar.FirstDocuments.value.length===0"></RecentFile>
    <TotalFile style="height: 300px" v-if="globalVar.FirstDocuments.value.length===0"></TotalFile>

  </el-drawer>
</template>

<script>
export default {
  name: "Header"
}
</script>

<script setup>

import {computed, ref} from "vue";
import {useStore} from "vuex";
import jump from "@/routers/jump";
import {Expand} from "@element-plus/icons-vue";
import DownloadTable from "@/components/Table/DownloadTable.vue";
import RecentFile from "@/components/eCharts/RecentFile.vue";
import TotalFile from "@/components/eCharts/TotalFile.vue";
import SecondDistribution from "@/components/eCharts/SecondDistribution.vue";
import SearchDistribution from "@/components/eCharts/SearchDistribution.vue";
import globalVar from "@/model/globalVar";
import HistoryTable from "@/components/Table/HistoryTable.vue";

const store = useStore()

const activeIndex = ref('2')
const clickIndex = ref('')
const draw = ref(false)
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

const isMobile = computed(() => window.screen.availWidth < 768)
const name = computed(() => store.state.LoginUser.name)

const loginOut = async function () {
  await store.dispatch("changeLoginStatus", false)
  globalVar.FileList.value = []
  globalVar.FileUpload.value = ''
  globalVar.FileParseList.value = []
  globalVar.FileSuccessList.value = []
  globalVar.FileDownloadList.value = []
  globalVar.FirstDocuments.value = []
  globalVar.SearchContent = null
  globalVar.SecondDocuments.value = []
  await jump.jumpToLogin()
}


const jumpToDocument = async function () {
  await jump.jumpToDocument()
}


const jumpToMain = async function () {
  await jump.jumpToMain()
}

const jumpToCollect = async () => {
  await jump.jumpToCollection()
}
const drawerControl = function () {
  draw.value = true
}

const jumpToBrowsing = async ()=>{
  await jump.jumpToBrowsing()
}
</script>

<style scoped>

.flex-grow {
  flex-grow: 1;
}


.custom-calendar /deep/ .el-calendar-table .el-calendar-day {
  width: 40px;
  height: 30px;
  font-size: 14px;
}
</style>