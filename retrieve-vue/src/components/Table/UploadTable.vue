<template>
  <FileOperation v-if="isMobile"></FileOperation>
  <el-row justify="space-between" align="middle">
    <el-col :span="10">
      <h3>待上传列表</h3>
    </el-col>
    <el-col :span="14" align="right">
      <el-button type="primary" @click="fileSubmit" v-if="!multiple">上传</el-button>
      <el-button type="warning" @click="handleMultiple" v-if="!multiple">批量删除</el-button>
      <el-button type="success" @click="handleMultiple" v-if="multiple">取消</el-button>
      <el-button type="danger" @click="removeAll" v-if="multiple">删除</el-button>
    </el-col>
    <el-col>

    </el-col>
  </el-row>
  <el-table
      :data="globalVar.FileList.value.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      empty-text="暂无待上传文件"
      ref="tableRef"
      style="width: 100%">
    <el-table-column type="selection" width="50" v-if="multiple"/>
    <el-table-column prop="name" label="文档" min-width="52.5%"/>
    <el-table-column label="状态" min-width="27.5%">
      <template v-slot="scope">
        <el-progress
            :text-inside="true"
            :stroke-width="22"
            :percentage="scope.row.percentage"
            :status="scope.row.status"
        />
      </template>
    </el-table-column>
    <el-table-column label="操作" min-width="20%" align="right">
      <template v-slot="scope">
        <el-button type="danger" :icon="Delete" circle @click="fileRemove(scope.row)"/>
      </template>
    </el-table-column>
  </el-table>


  <!--  分页-->
  <p>
    <el-pagination
        background layout="prev, pager, next"
        :total="globalVar.FileList.value.length"
        v-model:current-page="currentPage"
        default-current-page="1"
        :page-size="pagesize">
    </el-pagination>
  </p>
</template>

<script>


export default {
  name: "TablePC",

}
</script>

<script setup>
import globalVar from "@/model/globalVar";
import {Delete} from "@element-plus/icons-vue";
import {computed, ref, toRaw} from "vue";
import FileUpload from "@/components/Page/FileUpload.vue";
import {ElMessage} from "element-plus";
import FileOperation from "@/components/Page/FileUpload.vue";

//操作表格的响应式变量
const tableRef = ref()
//是否显示批量操作
const multiple = ref(false)

const fileSubmit = function () {
  if ((globalVar.FileList.value).length === 0) {
    ElMessage({
      message: '没有待上传的文件',
      type: 'warning',
    })
  } else {
    globalVar.FileUpload.value.submit()
  }
}


const isMobile = computed(() => window.screen.availWidth < 768)


/*
用作列表分页
 */
//当前页
const currentPage = ref(1)
//每一页显示的项目数
const pagesize = ref(5)


//手动移除文件的方法
const fileRemove = (file) => {
  globalVar.FileUpload.value.handleRemove(file)
}

//批量操作开关
const handleMultiple = () => {
  multiple.value = !multiple.value
}


//批量删除
const removeAll = () => {
  const files = tableRef.value.getSelectionRows()
  console.log(files)
  let file
  for (file in files) {
    globalVar.FileUpload.value.handleRemove(file)
  }
}


</script>

<style scoped>

</style>