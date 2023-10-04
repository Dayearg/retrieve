<template>
  <el-row justify="space-between" align="middle">
    <el-col :span="8">
      <h3>已解析列表</h3>
    </el-col>
    <el-col :span="16" align="right">
      <el-button type="success" v-if="!multiple" @click="refresh">刷新</el-button>
      <el-button type="warning" @click="handleMultiple" v-if="!multiple">批量操作</el-button>
      <el-button type="success" @click="handleMultiple" v-if="multiple">取消</el-button>
      <el-button type="danger" @click="remove" v-if="multiple">删除</el-button>
      <el-button type="primary" v-if="multiple" @click="downloadAll">下载</el-button>
    </el-col>
    <el-col>

    </el-col>
  </el-row>

  <el-table
      :data="filterTableData.reverse()"
      style="width: 100%"
      @row-dblclick="preview"
      ref="tableRef"
      empty-text="所有文件均已解析"
      v-if="!isMobile"
      height="500"
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
    <el-table-column prop="tags" label="关键词" min-width="20%" header-align="center" align="left">
      <template #default="scope">
        <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[1]"></EditableTag>
      </template>
    </el-table-column>
    <el-table-column prop="uploadTime" label="上传时间" min-width="8%" align="center"/>
    <el-table-column prop="download" label="下载" min-width="5%" align="center"/>
    <el-table-column label="状态" min-width="5%" align="center">
      <template #default="scope">
        <StatusTag v-model:status-int="scope.row.status"></StatusTag>
      </template>
    </el-table-column>
    <el-table-column align="right" min-width="15%">
      <template #header>
        <el-input v-model="search" placeholder="搜索文档"/>
      </template>
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
            circle></el-button>
        <el-button
            v-if="scope.row.status===0"
            size="default"
            type="success"
            :icon="Lock"
            @click="changePermissions(scope.row)"
            circle></el-button>
        <el-button
            v-if="scope.row.status===1"
            size="default"
            type="warning"
            :icon="Unlock"
            @click="changePermissions(scope.row)"
            circle></el-button>
        <el-button
            size="default"
            type="warning"
            :icon="Download"
            @click="download(scope.row)"
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


  <el-table
      style="width: 100%"
      empty-text="所有文件均已解析"
      :data="globalVar.FileSuccessList.value.reverse()"
      v-if="isMobile"
      height="500"
      :row-key="
        (row) => {
          return row.title
        }
      "
      :expand-row-keys="expandedRowKeys"
      @expand-change="expandOpen"
  >
    <el-table-column type="selection" width="50" v-if="multiple" align="center"/>
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
                            placeholder="选择日期"
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
          <p>下载次数：
            {{ scope.row.download }}
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
                :icon="Check"></el-button>
            <el-button
                v-if="scope.row.status===0"
                size="default"
                type="success"
                :icon="Lock"
                @click="changePermissions(scope.row)"
            ></el-button>
            <el-button
                v-if="scope.row.status===1"
                size="default"
                type="warning"
                :icon="Unlock"
                @click="changePermissions(scope.row)"
            ></el-button>
            <el-button
                size="default"
                type="warning"
                :icon="Download"
                @click="download(scope.row)"
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

</template>

<script>
export default {
  name: "AnalyzeTable",

}
</script>

<script setup>
import globalVar from "@/model/globalVar";
import {computed, onMounted, ref, toRaw} from "vue";
import {Check, Delete, Download, Edit, Lock, Unlock} from "@element-plus/icons-vue";
import DocumentService from "@/services/DocumentService";
import EditableTag from "@/components/Card/EditableTag.vue";
import StatusTag from "@/components/Card/StatusTag.vue";
import {useStore} from "vuex";
import doc from "@/items/doc";
import newPage from "@/routers/newPage";
import {ElMessage} from "element-plus";


//判断是否是移动端
const isMobile = computed(() => window.screen.availWidth < 768)

//操作表格的响应式变量
const tableRef = ref()
//是否显示批量操作
const multiple = ref(false)


const search = ref('')
const filterTableData = computed(() =>
    globalVar.FileSuccessList.value.filter(
        (data) =>
            !search.value ||
            data.title.toLowerCase().includes(search.value.toLowerCase())
    )
)

//批量操作开关
const handleMultiple = () => {
  multiple.value = !multiple.value
}


function removeElement(list, element) {
  return list.filter(item => item !== element);
}

const remove = () => {
  let list = tableRef.value.getSelectionRows()
  const res = DocumentService.delete(list)
  console.log(res)
  for (let i = 0; i < list.length; i++) {
    globalVar.FileSuccessList.value = removeElement(globalVar.FileSuccessList.value, list[i])
  }
}


const removeRow = (row) => {
  let list = []
  list.push(row)
  const res = DocumentService.delete(list)
  console.log(res)
  globalVar.FileSuccessList.value = removeElement(globalVar.FileSuccessList.value, row)
}


const store = useStore()

const name = store.state.LoginUser.name

const refresh = async () => {
  const res = await DocumentService.refresh(name)
  globalVar.FileSuccessList.value = []
  let response = null
  for (response of res) {
    globalVar.FileSuccessList.value.push(new doc(response.documentId, response.title, response.authors, response.uploadUser, response.published, response.tags, response.uploadTime, response.download, response.fileName, response.status,response.click))
  }
}

const labels = ['authors', 'tags']


const preview = async (row, column, event) => {
  const res = await DocumentService.preview(toRaw(row))
  newPage(res)
}

const download = (row) => {
  let documents = []
  documents.push(toRaw(row))
  globalVar.FileDownloadList.value.push(toRaw(row))
  globalVar.FileDownloadList.value[0].downloadStatus = true
  console.log(documents)
  DocumentService.download(documents)
}

const downloadAll = () => {
  console.log(tableRef.value.getSelectionRows())
  DocumentService.download(tableRef.value.getSelectionRows())
}


const check = (row) => {
  row.editable = false
  DocumentService.revise(row)
}

const changePermissions = (row) => {
  if (row.status === 1) {
    row.status = 0
    ElMessage({
      message: '所有人均可检索',
      type: 'success',
    })
  } else if (row.status === 0) {
    row.status = 1
    ElMessage({
      message: '仅当前用户检索',
      type: 'warning',
    })
  }
  DocumentService.revise(row)

}

onMounted(
    () => {
      refresh()
    }
)

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