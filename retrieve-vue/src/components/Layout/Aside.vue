<template>
  <el-menu
      class="el-menu-vertical-demo"
      ref="menu"
      style="width: 95%;margin-left:20px"
      :default-openeds="[1,2]"
  >
    <el-collapse-transition>
      <FileOperation v-if="$route.name==='Document'"></FileOperation>
      <el-calendar class="custom-calendar" v-if="$route.name==='History'" v-model="globalVar.Calendar.value" >
        <template #header="{ date }">
          <span>{{ date }}</span>
          <el-button-group>
          </el-button-group>
        </template>
      </el-calendar>
    </el-collapse-transition>
    <el-sub-menu :index="1">
      <template #title>
        <el-icon>
          <View/>
        </el-icon>
        <span>浏览记录</span>
      </template>
      <!--      <DownloadTable></DownloadTable>-->
      <HistoryTable></HistoryTable>
    </el-sub-menu>
    <el-sub-menu :index="2">
      <template #title>
        <el-icon>
          <DataAnalysis/>
        </el-icon>
        <span>数据统计</span>
      </template>
      <div style="margin-top: 10px">
        <SecondDistribution
            v-if="$route.name==='SecondSearch' && globalVar.SecondDocuments.value.length!==0"></SecondDistribution>
        <SearchDistribution v-if="globalVar.FirstDocuments.value.length!==0"></SearchDistribution>
        <RecentFile style="height: 300px" v-if="globalVar.FirstDocuments.value.length===0"></RecentFile>
        <TotalFile style="height: 300px" v-if="globalVar.FirstDocuments.value.length===0"></TotalFile>
      </div>
    </el-sub-menu>
  </el-menu>

</template>

<script>
import FileOperation from "@/components/Page/FileUpload.vue";

export default {
  name: "Aside",
  components: {FileOperation}
}
</script>

<script setup>
import {computed, onMounted, ref} from "vue";
import DownloadTable from "@/components/Table/DownloadTable.vue";
import SearchDistribution from "@/components/eCharts/SearchDistribution.vue";
import globalVar from "@/model/globalVar";
import SecondDistribution from "@/components/eCharts/SecondDistribution.vue";
import RecentFile from "@/components/eCharts/RecentFile.vue";
import TotalFile from "@/components/eCharts/TotalFile.vue";
import HistoryTable from "@/components/Table/HistoryTable.vue";

const menu = ref('')

onMounted(
    () => {
      setTimeout(function () {
        menu.value.open('2')
      }, 1200);
    }
)

</script>

<style scoped>
.el-menu-item,
::v-deep .el-sub-menu .el-sub-menu__title {
  color: gray;

}


.custom-calendar /deep/ .el-calendar-table .el-calendar-day {
  width: 40px;
  height: 30px;
  font-size: 14px;
}
</style>