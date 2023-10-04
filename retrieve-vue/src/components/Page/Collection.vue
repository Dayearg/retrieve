<template>
  <el-row justify="space-between" align="middle">
    <el-col :span="8">
      <h3>收藏</h3>
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
      :data="docs"
      style="width: 100%"
      @row-dblclick="preview"
      ref="tableRef"
      empty-text="暂无文档"
      v-if="!isMobile"
  >
    <el-table-column type="selection" width="50" v-if="multiple"/>
    <el-table-column prop="title" label="标题" min-width="20%"/>
    <el-table-column label="作者" min-width="17%">
      <template #default="scope">
        <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[0]"></EditableTag>
      </template>
    </el-table-column>
    <el-table-column label="发表时间" min-width="10%">
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
    <el-table-column prop="tags" label="标签" min-width="20%">
      <template #default="scope">
        <EditableTag v-model:documentRef="scope.row" v-model:labelRef="labels[1]"></EditableTag>
      </template>
    </el-table-column>
    <el-table-column prop="uploadTime" label="上传时间" min-width="8%"/>
    <el-table-column prop="download" label="下载" min-width="5%"/>
    <el-table-column prop="uploadUser" label="上传用户" min-width="8%"/>

    <el-table-column align="right" min-width="12%">
      <template #header>
        <el-input size="default" placeholder="搜索文档"/>
      </template>
      <template #default="scope">
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
      :data="docs"
      v-if="isMobile"
  >
    <el-table-column type="selection" width="50" v-if="multiple"/>
    <el-table-column prop="title" label="标题" min-width="70"/>
    <el-table-column label="状态" min-width="20%">
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
          <p>下载次数：
            {{ scope.row.download }}
          </p>
          <p align="right">
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
  name: "Collection"
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


//判断是否是移动端
const isMobile = computed(() => window.screen.availWidth < 768)

//操作表格的响应式变量
const tableRef = ref()
//是否显示批量操作
const multiple = ref(false)

const docs = ref([])


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
  for (let i = 0; i < list.length; i++) {
    docs.value = removeElement(docs.value, list[i])
  }
}


const removeRow = (row) => {
  let list = []
  list.push(row)
  const res = DocumentService.delete(list)
  docs.value = removeElement(docs.value, row)
}


const store = useStore()

const name = store.state.LoginUser.name

const refresh = async () => {
  const res = await DocumentService.loadCollection(name)
  docs.value = []
  let response = null
  for (response of res) {
    docs.value.push(new doc(response.documentId, response.title, response.authors, response.uploadUser, response.published, response.tags, response.uploadTime, response.download, response.fileName, response.status,response.click))
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
  DocumentService.download(documents)
}

const downloadAll = () => {
  console.log(tableRef.value.getSelectionRows())
  DocumentService.download(tableRef.value.getSelectionRows())
}

onMounted(
    () => {
      refresh()
      console.log(docs.value)
    }
)
</script>

<style scoped>

</style>