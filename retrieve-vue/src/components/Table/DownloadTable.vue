<template>
  <el-table
      :data="toRaw(globalVar.FileDownloadList.value)"
      style="width: 100%"
      empty-text="暂无下载记录"
  >
    <el-table-column prop="title" label="文档" min-width="52.5%" align="center"/>
    <el-table-column label="状态" min-width="20%" align="left" header-align="center" fixed="right">
      <template v-slot="scope">
        <el-tag type="success">成功</el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>

export default {
  name: "DownloadTable",

}
</script>

<script setup>
import {computed, toRaw} from "vue";
import globalVar from "@/model/globalVar";
import {tag} from "postcss-selector-parser";


</script>

<style scoped>

</style>