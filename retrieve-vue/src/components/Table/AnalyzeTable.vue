<template>
  <el-row justify="space-between" align="middle">
    <el-col :span="8">
      <h3>当前解析列表</h3>
    </el-col>
    <el-col :span="16" align="right">
      <el-button type="primary" v-if="!multiple" @click="removeAll">清空</el-button>
      <el-button type="warning" @click="handleMultiple" v-if="!multiple">批量操作</el-button>
      <el-button type="success" @click="handleMultiple" v-if="multiple">取消</el-button>
      <el-button type="danger" @click="remove" v-if="multiple">删除</el-button>
    </el-col>
    <el-col>

    </el-col>
  </el-row>

  <!--  桌面端列表-->
  <el-table
      style="width: 100%"
      ref="tableRef"
      empty-text="所有文件均已解析"
      :data="globalVar.FileParseList.value.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      v-if="!isMobile"
  >
    <el-table-column type="selection" width="50" v-if="multiple" align="center"/>
    <el-table-column prop="title" label="标题" min-width="20%" align="left" header-align="center"/>
    <el-table-column label="作者" min-width="17%" align="left" header-align="center">
      <template #default="scope">
        <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[0]"></EditableTag>
      </template>
    </el-table-column>
    <el-table-column label="发表时间" min-width="10%" align="center">
      <template #default="scope">
        <el-date-picker v-if="scope.row.editable"
                        v-model="scope.row.published"
                        type="date"
                        size="default"
                        placeholder="Pick a day"
                        style="width: 100%"
                        value-format="YYYY-MM-DD"
        />
        <div v-else>
          {{ scope.row.published }}
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="tags" label="关键词" min-width="20%" align="left" header-align="center">
      <template #default="scope">
        <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[1]"></EditableTag>
      </template>
    </el-table-column>
    <el-table-column prop="uploadTime" label="上传时间" min-width="8%" align="center"/>
    <el-table-column label="状态" min-width="5%" align="center">
      <template #default="scope">
        <StatusTag v-model:status-int="scope.row.status"></StatusTag>
      </template>
    </el-table-column>
    <el-table-column align="center" min-width="15%" label="操作">
      <template #default="scope">
        <el-button
            v-if="scope.row.editable===false"
            @click="scope.row.editable=true"
            size="default"
            type="primary"
            :icon="Edit"
            circle
        ></el-button>
        <el-button
            v-if="scope.row.editable===true"
            @click="check(scope.row)"
            size="default"
            type="warning"
            :icon="Check"
            circle
        ></el-button>
        <el-button
            size="default"
            type="danger"
            :icon="Delete"
            @click="removeRow(scope.row)"
            circle
        ></el-button>
      </template>
    </el-table-column>
  </el-table>

  <!--  移动端列表-->
  <el-table
      style="width: 100%"
      empty-text="所有文件均已解析"
      :data="globalVar.FileParseList.value.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      v-if="isMobile"
      :row-key="
        (row) => {
          return row.title
        }
      "
      :expand-row-keys="expandedRowKeys"
      @expand-change="expandOpen"
  >
    <el-table-column prop="title" label="标题" min-width="70" align="left" header-align="center"/>
    <el-table-column label="状态" min-width="20%" align="center">
      <template #default="scope">
        <StatusTag v-model:status-int="scope.row.status"></StatusTag>
      </template>
    </el-table-column>

    <el-table-column type="expand" min-width="10%">
      <template #default="scope">
        <div style="margin-left: 10px">
          <p>文献作者：
          </p>
          <p>
            <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[0]"></EditableTag>
          </p>
          <p>发表时间：
            <el-date-picker v-if="scope.row.editable"
                            v-model="scope.row.published"
                            type="date"
                            size="default"
                            placeholder="Pick a day"
                            style="width: 50%"
                            value-format="YYYY-MM-DD"
            />
            <template v-else>
              {{ scope.row.published }}
            </template>
          </p>
          <p>文献标签：
          </p>
          <p>
            <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[1]"></EditableTag>
          </p>
          <p>上传时间：
            {{ scope.row.uploadTime }}
          </p>
          <p align="right">
            <el-button
                v-if="scope.row.editable===false"
                @click="scope.row.editable=true"
                size="default"
                type="primary"
                :icon="Edit"
            ></el-button>
            <el-button
                v-if="scope.row.editable===true"
                @click="check(scope.row)"
                size="default"
                type="warning"
                :icon="Check"
            ></el-button>
            <el-button
                size="default"
                type="danger"
                :icon="Delete"
                @click="removeRow(scope.row)"
            ></el-button>
          </p>
        </div>
      </template>
    </el-table-column>
  </el-table>

  <!--  分页-->
  <p>
    <el-pagination
        background layout="prev, pager, next"
        :total="globalVar.FileParseList.value.length"
        v-model:current-page="currentPage"
        default-current-page="1"
        :page-size="pagesize">
    </el-pagination>
  </p>
</template>

<script>
export default {
  name: "AnalyzeTable",

}
</script>

<script setup>
import globalVar from "@/model/globalVar";
import {computed, ref, toRaw} from "vue";
import {Check, DataAnalysis, Delete, Download, Edit} from "@element-plus/icons-vue";
import DocumentService from "@/services/DocumentService";
import EditableTag from "@/components/Card/EditableTag.vue";
import StatusTag from "@/components/Card/StatusTag.vue";


//判断是否是移动端
const isMobile = computed(() => window.screen.availWidth < 768)

//操作表格的响应式变量
const tableRef = ref()
//是否显示批量操作
const multiple = ref(false)


/*
用作列表分页
 */
//当前页
const currentPage = ref(1)
//每一页显示的项目数
const pagesize = ref(5)

//批量操作开关
const handleMultiple = () => {
  multiple.value = !multiple.value
}


//清空列表
const removeAll = () => {
  globalVar.FileParseList.value = []
}


function removeElement(list, element) {
  return list.filter(item => item !== element);
}

const remove = () => {
  let list = tableRef.value.getSelectionRows()
  const res = DocumentService.delete(list)
  console.log(res)
  for (let i = 0; i < list.length; i++) {
    globalVar.FileParseList.value = removeElement(globalVar.FileParseList.value, list[i])
  }
}


const removeRow = (row) => {
  let list = []
  list.push(row)
  const res = DocumentService.delete(list)
  console.log(res)
  globalVar.FileParseList.value = removeElement(globalVar.FileParseList.value, row)
}

const labels = ['authors', 'tags']

const check = (row) => {
  row.editable = false
  DocumentService.revise(row)
}


const expandedRowKeys = ref([])
const close = function (array, val) {
  const index = array.indexOf(val)
  if (index > -1) {
    array.splice(index, 1)
    return true
  }
  return false
}
const expandOpen = async (row, expand) => {
  console.log(row, expand)
  if (!close(expandedRowKeys.value, row.title)) {
    expandedRowKeys.value.push(row.title)
  }
}

</script>

<style scoped>

</style>