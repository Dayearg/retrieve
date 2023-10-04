<template>
  <el-upload
      class="upload-demo"
      style="margin: 10px"
      drag
      :action="url"
      multiple
      :show-file-list=false
      v-model:file-list="globalVar.fileList"
      :auto-upload="false"
      :data="{username:store.state.LoginUser.name}"
      :on-change="updateList"
      :on-success="uploadSuccess"
      :ref="globalVar.FileUpload">
    <el-icon class="el-icon--upload">
      <upload-filled/>
    </el-icon>
    <div class="el-upload__text">
      拖拽文件到此处或 <em>点击上传文件</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        仅限PDF，大小不超过100MB
      </div>
    </template>
  </el-upload>
</template>

<script>
export default {
  name: "FileOperation",
}
</script>

<script setup>
import {ref, toRaw} from "vue";
import {UploadFilled} from '@element-plus/icons-vue'
import store from "@/store";
import globalVar from "@/model/globalVar";
import doc from "@/items/doc";
import {ElMessage} from "element-plus";

const url = ref('http://127.0.0.1:8081/file/upload')


//更新待上传文件列表
const updateList = function (uploadFile, uploadFiles) {
  globalVar.FileList.value = uploadFiles

}

//文件上传成功的调用
const uploadSuccess = function (response, file, files) {
  globalVar.FileParseList.value.push(new doc(response.documentId, response.title, response.authors, response.uploadUser, response.published, response.tags, response.uploadTime, response.download, response.fileName, response.status,response.click))
  console.log(response)
  globalVar.FileUpload.value.handleRemove(file)
  if (files.length === 0) {
    ElMessage({
      message: '所有文件均上传成功！',
      type: 'success',
    })
  }
}

</script>

<style scoped>

</style>