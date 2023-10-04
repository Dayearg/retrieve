<template>
<el-table
      :data="globalVar.localHistory.value"
      style="width: 100%"
      empty-text="暂无浏览记录"
      @row-click="detail"
  >
    <el-table-column prop="title" label="文档" min-width="52.5%" align="center"/>
  </el-table>
</template>

<script setup>

import {toRaw} from "vue";
import globalVar from "@/model/globalVar";
import DocumentService from "@/services/DocumentService";
import newPage from "@/routers/newPage";
import jump from "@/routers/jump";

const detail = async (row)=>{
  console.log(toRaw(row.content))
  await jump.jumpToDetail(toRaw(row.firstDocument),toRaw(row.searchContent))
}
</script>



<style scoped>

</style>