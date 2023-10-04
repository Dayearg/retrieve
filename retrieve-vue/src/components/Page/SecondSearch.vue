<template>
  <p>
  </p>
  <el-collapse-transition>
    <ResCard :search-res="firstDocument" style="margin: 0 auto;max-width: 1000px"></ResCard>
  </el-collapse-transition>

  <el-row style="max-width: 1000px;margin: 0 auto;">
     <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
       <Score :first-document="firstDocument"></Score>
     </el-col>
    <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
       <p style="text-align: right;max-width: 1000px;margin:  15px auto">
    <el-button type="primary" :icon="Download" @click="download(firstDocument.title,firstDocument.uploadUser)">下载
    </el-button>
    <el-button type="success" :icon="View" @click="preview(firstDocument.title,firstDocument.uploadUser,0)">预览
    </el-button>
    <el-button type="warning" :icon="Star" @click="collect(name,firstDocument.title,firstDocument.uploadUser)">收藏
    </el-button>
  </p>
  <p style="text-align: right;max-width: 1000px;margin: 0 auto;">
    <el-input v-model="detailSearch" style="margin: 0 auto;max-width: 265px" placeholder="在此篇文章中搜索"
              @keydown.enter.native="getDeatailSearch">
      <template #prefix>
        <el-icon class="el-input__icon">
          <search/>
        </el-icon>
      </template>
    </el-input>
  </p>
  <p style="text-align: right;max-width: 1000px;margin:  15px auto">
    <el-switch
        v-model="switchValue"
        size="large"
        active-text="图片"
        inactive-text="图片和文本"
        @change="changeStatus"
    />
  </p>
    </el-col>
  </el-row>

  <el-row :justify="'center'">
    <template v-for="res in searchRes">
      <el-divider v-if="showPage(res.page)" content-position="left" style="max-width: 1000px;" >
        <!--      <span style="color: #409EFF;font-weight:bold;"></span>-->
        <el-tag effect="dark" type="warning" >第{{ res.page }}页</el-tag>
      </el-divider>
      <DetailCard :search-res="res" style="margin: 5px auto;max-width: 1000px"
                  @click="preview(firstDocument.title,firstDocument.uploadUser,res.page)"></DetailCard>
    </template>
  </el-row>

</template>

<script>
import ResCard from "@/components/Card/ResCard.vue";

export default {
  name: "SecondSearch",
  components: {ResCard}
}
</script>

<script setup>
import {useRouter} from "vue-router";
import {computed, onMounted, ref, toRaw} from "vue";
import SearchService from "@/services/SearchService";
import DetailCard from "@/components/Card/DetailCard.vue";
import globalVar from "@/model/globalVar";
import {Download, Search, Star, View} from "@element-plus/icons-vue";
import DocumentService from "@/services/DocumentService";
import newPage from "@/routers/newPage";
import searchContent from "@/items/searchContent";
import {useStore} from "vuex";
import jump from "@/routers/jump";
import {ElMessage} from "element-plus";
import Score from "@/components/eCharts/Score.vue";

const store = useStore()
const name = computed(() => store.state.LoginUser.name)
const router = useRouter()
const firstDocument = ref(JSON.parse(router.currentRoute.value.query.firstDocument))
const content = ref(JSON.parse(router.currentRoute.value.query.searchContent))
const searchRes = ref()
const switchValue = ref(false)
const detailSearch = ref('')
const username=store.state.LoginUser.name



//断页操作
let currentPage = ref(0)

const showPage = (page) => {
  if (currentPage.value !== page) {
    currentPage.value = page
    return true
  } else {
    return false
  }
}


const secondSearch = async () => {
  await SearchService.secondSearch({...toRaw(firstDocument.value), searchContents: content.value,username: username}).then(res => {
    searchRes.value = res
    globalVar.SecondDocuments.value = searchRes.value
    addObjectToArray(globalVar.localHistory.value,{title:firstDocument.value.title,searchContent:content,firstDocument:firstDocument.value})
  })
}
const collect = async (name, title, uploadUser) => {
  await DocumentService.collect({"username": name, "uploadUser": uploadUser, "title": title})
  ElMessage({
    message: '收藏成功！',
    type: 'success',
  })
}
const getPicture = async () => {
  const content = [new searchContent("title", true, firstDocument.value.title, null, false), new searchContent("source", false, 1, null, true)]
  await SearchService.secondSearch({...toRaw(firstDocument.value), searchContents: content}).then(res => {
    searchRes.value = res
    globalVar.SecondDocuments.value = searchRes.value
  })
}

const getDeatailSearch = async () => {
  const content = [new searchContent("default", true, detailSearch.value, null, false)]
  await SearchService.secondSearch({...toRaw(firstDocument.value), searchContents: content}).then(res => {
    searchRes.value = res
    globalVar.SecondDocuments.value = searchRes.value
  })
}

const changeStatus = async () => {
  console.log("回调")
  if (switchValue.value === false) {
    await secondSearch()
  } else if (switchValue.value === true) {
    await getPicture()
  }
}
onMounted(
    () => {
      secondSearch()
    }
)

const preview = async (title, uploadUser, page) => {
  const res = await DocumentService.preview({title: title, uploadUser: uploadUser})
  if (page !== 0) {
    newPage(res + '#page=' + page)
  } else {
    newPage(res)
  }
}

const download = (title, uploadUser) => {
  let documents = []
  documents.push({title: title, uploadUser: uploadUser})
  globalVar.FileDownloadList.value.push({title: title, uploadUser: uploadUser})
  globalVar.FileDownloadList.value[0].downloadStatus = true
  DocumentService.download(documents)
}

const addObjectToArray = (array, object) => {
  const index = array.findIndex((item) => item.title === object.title);

  if (index !== -1) {
    return 0;
  }

  if (array.length >= 10) {
    array.shift();
  }

  array.push(object);
  return array.length;
};
</script>

<style scoped>

</style>