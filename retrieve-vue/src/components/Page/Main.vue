<template>
  <el-row justify="end">
    <el-switch
    v-model="searchContent[0].all"
    class=""
    inline-prompt
    active-text="全文数据库"
    inactive-text="个人数据库"
    style="padding: 9px 0;--el-switch-on-color: #67C23A; --el-switch-off-color: #E6A23C"
    size="large"
  /></el-row>
  <div style="height: 100%;width: 100%;" class="blur">
    <el-row justify="center" style="">
      <img src="../../../public/webIcon/headlogorere.png" v-if="searchRes.length===0"
           style="margin-top: 12%;height: 80px;margin-bottom: 50px"/>
    </el-row>
    <!--  搜索框-->
    <div style="margin: 20px auto;text-align: center;max-width: 1000px; " :key="reKey" v-if="!isMobile()">
      <el-button :icon="Plus" type="primary" plain @click="addContent" style="width: 85px" size="large"
                 v-if="!isMobile()" class="font-color"></el-button>
      <el-select size="large" placeholder="默认" style="width: 80px;margin-left: 10px" v-model="searchContent[0].type"
                 class="font-color">
        <el-option label="默认" value="default"/>
        <el-option label="标题" value="title"/>
        <el-option label="文本" value="text"/>
        <el-option label="作者" value="authors"/>
        <el-option label="标签" value="tags"/>
      </el-select>
      <el-autocomplete
          style="width: 65%;padding-left: 10px;"
          @select="item => afterSelect(searchContent[0])"
          size="large"
          :fetch-suggestions="((queryString,cb)=>{querySearch(queryString,cb,searchContent[0])})"
          placeholder="在此处搜索"
          v-model="searchContent[0].content"
          @keyup.enter.native="firstSearch"
          :fit-input-width="true"
      >
        <template #append>
          <el-button :icon="Search" @click="firstSearch"/>
        </template>
      </el-autocomplete>
      <el-switch
          v-if="!isMobile()"
          v-model="searchContent[0].precise"
          size="large"
          active-text="精确"
          inactive-text="模糊"
          style="float:right;"
      />
    </div>
    <!--  高级搜索框-->
    <template v-for="sc in searchContent.slice(1,searchContent.length)">
      <div style="margin: 5px auto;max-width: 1000px;text-align: center" v-if="!isMobile()">
        <el-select size="large" placeholder="AND" v-model="sc.operation" style="width: 85px" class="font-color">
          <el-option label="AND" value="AND"/>
          <el-option label="OR" value="OR"/>
          <el-option label="NOT" value="NOT"/>
        </el-select>
        <el-select size="large" style="width: 80px;margin-left: 10px" v-model="sc.type" class="font-color">
          <el-option label="标题" value="title"/>
          <el-option label="文本" value="text"/>
          <el-option label="作者" value="authors"/>
          <el-option label="标签" value="tags"/>
        </el-select>
        <el-autocomplete
            style="width: 65%;padding-left: 10px;"
            size="large"
            :fetch-suggestions="((queryString,cb)=>{querySearch(queryString,cb,sc)})"
            placeholder="在此处搜索"
            v-model="sc.content"
            @keyup.enter.native="firstSearch"
            :fit-input-width="true"
        >
          <template #append>
            <el-button :icon="Close"/>
          </template>
        </el-autocomplete>
        <el-switch
            v-model="sc.precise"
            size="large"
            active-text="精确"
            inactive-text="模糊"
            style="float:right;"

        />
      </div>
    </template>

    <div style="width: 100%;margin: 20px auto;text-align: center;" :key="reKey" v-if="isMobile()">
      <el-row style="width: 100%;margin: 20px auto;text-align: center;" justify="space-between">
        <el-col :span="6">
          <el-button :icon="Plus" type="primary" plain @click="addContent" style="width: 100%" size="large"
                     class="font-color"></el-button>
        </el-col>
        <el-col :span="6">
          <el-select size="large" placeholder="默认" style="width: 80px;margin-left: 10px"
                     v-model="searchContent[0].type" class="font-color">
            <el-option label="默认" value="default"/>
            <el-option label="标题" value="title"/>
            <el-option label="文本" value="text"/>
            <el-option label="作者" value="authors"/>
            <el-option label="标签" value="tags"/>
          </el-select>
        </el-col>
        <el-col :span="12">
          <el-switch
              v-model="searchContent[0].precise"
              size="large"
              active-text="精确"
              inactive-text="模糊"
              style="float:right;"
          />
        </el-col>
      </el-row>
      <el-row>
        <el-autocomplete
            style="width: 100%"
            size="large"
            :fetch-suggestions="((queryString,cb)=>{querySearch(queryString,cb,searchContent[0])})"
            placeholder="在此处搜索"
            v-model="searchContent[0].content"
            @keyup.enter.native="firstSearch"
            :fit-input-width="true"
        >
          <template #append>
            <el-button :icon="Search" @click="firstSearch"/>
          </template>
        </el-autocomplete>
      </el-row>
    </div>

    <template v-for="sc in searchContent.slice(1,searchContent.length)">
      <el-divider v-if="isMobile()"/>
      <div style="width: 100%;margin: 20px auto;text-align: center;" :key="reKey" v-if="isMobile()">
        <el-row style="width: 100%;margin: 20px auto;text-align: center;" justify="space-between">
          <el-col :span="6">
            <el-select size="large" placeholder="AND" v-model="sc.operation" style="width: 85px" class="font-color">
              <el-option label="AND" value="AND"/>
              <el-option label="OR" value="OR"/>
              <el-option label="NOT" value="NOT"/>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select size="large" style="width: 80px;margin-left: 10px" v-model="sc.type" class="font-color">
              <el-option label="标题" value="title"/>
              <el-option label="文本" value="text"/>
              <el-option label="作者" value="authors"/>
              <el-option label="标签" value="tags"/>
            </el-select>
          </el-col>
          <el-col :span="12">
            <el-switch
                v-model="sc.precise"
                size="large"
                active-text="精确"
                inactive-text="模糊"
                style="float:right;"

            />
          </el-col>
        </el-row>
        <el-row>
          <el-autocomplete
              style="width: 100%"
              size="large"
              placeholder="在此处搜索"
               :fetch-suggestions="((queryString,cb)=>{querySearch(queryString,cb,sc)})"
              v-model="sc.content"
              @keyup.enter.native="firstSearch"
               :fit-input-width="true"
          >
            <template #append>
              <el-button :icon="Close"/>
            </template>
          </el-autocomplete>
        </el-row>
      </div>
    </template>
    <div v-if="searchRes.length!==0" style="margin: 0 auto;max-width: 1000px;padding-bottom: 15px">
      <el-row :gutter="0" justify="end">
    <el-col :xs="4" :sm="4" :md="4" :lg="2" :xl="2" align="right">
      <el-button type="primary"  size="small" @click="searchContent[0].sortIndex=0;firstSearch()">默认</el-button>
    </el-col>
    <el-col :xs="4" :sm="4" :md="2" :lg="2" :xl="2" align="right">
      <el-button type="success"  size="small" @click="searchContent[0].sortIndex=1;firstSearch()">下载</el-button>
    </el-col>
    <el-col :xs="4" :sm="4" :md="2" :lg="2" :xl="2" align="right">
      <el-button type="danger"  size="small" @click="searchContent[0].sortIndex=2;firstSearch()">收藏</el-button>
      </el-col>
    <el-col :xs="4" :sm="4" :md="2" :lg="2" :xl="2" align="right">
      <el-button type="warning"  size="small" @click="searchContent[0].sortIndex=3;firstSearch()">点击</el-button>
        </el-col>
  </el-row>
    </div>

    <div style="margin: 0 auto;max-width: 600px;padding-top: 5%; " v-if="searchRes.length===0">
      <el-row justify="center" gutter="30">
        <template v-for="item in iconInfo">
          <el-col :xs="5" :sm="5" :md="5" :lg="5" :xl="5">
            <IconCard :item="item"></IconCard>
          </el-col>
        </template>
      </el-row>
    </div>

    <div>
      <el-row :justify="'center'">
        <template v-for="res in searchRes">
          <ResCard :search-res="res" style="margin: 5px auto;" @click="secondSearch(res)"></ResCard>
        </template>
      </el-row>
    </div>
  </div>
</template>

<script>


export default {
  name: "Main",
  computed: {},
  components: {}
}
</script>

<script setup>


import {Close, Plus, Search} from "@element-plus/icons-vue";
import {computed, onMounted, ref, toRaw} from "vue";
import SearchContent from "@/items/searchContent";
import ResCard from "@/components/Card/ResCard.vue";
import SearchService from "@/services/SearchService";
import jump from "@/routers/jump";
import globalVar from "@/model/globalVar";
import TotalFile from "@/components/eCharts/TotalFile.vue";
import RecentFile from "@/components/eCharts/RecentFile.vue";
import IconCard from "@/components/Card/IconCard.vue";
import {ElMessage} from "element-plus";
import store from "@/store";
import HistoryService from "@/services/HistoryService";
import searchHistory from "@/items/searchHistory";
import HistoryItem from "@/items/HistoryItem";

const iconInfo = ref(
    [
      {
        title: "CAMS",
        image: require("../../../public/webIcon/中国生物医学文献服务系统.png"),
        url: "www.sinomed.ac.cn/index.jsp",

      },
      {
        title: "万方",
        image: require("../../../public/webIcon/万方医学网.png"),
        url: "med.wanfangdata.com.cn"
      },
      {
        title: "Embase",
        image: require("../../../public/webIcon/Embase.png"),
        url: "www.embase.com/landing?status=grey"
      },
      {
        title: "NSTL",
        image: require("../../../public/webIcon/NSTL.png"),
        url: "www.nstl.gov.cn"
      },
      {
        title: "知网",
        image: require("../../../public/webIcon/中国知网.png"),
        url: "www.cnki.net/"
      },
      {
        title: "PubMed",
        image: require("../../../public/webIcon/PubMed.png"),
        url: "pubmed.ncbi.nlm.nih.gov/"
      },
      {
        title: "Cochrane",
        image: require("../../../public/webIcon/CochraneLibrary.png"),
        url: "cochranelibrary.com"
      },
      {
        title: "Ovid",
        image: require("../../../public/webIcon/Ovid.png"),
        url: "ovidsp.ovid.com"
      },
      {
        title: "PLOS",
        image: require("../../../public/webIcon/PLOS.png"),
        url: "plos.org"
      },
      {
        title: "Sci-Hub",
        image: require("../../../public/webIcon/Sci-Hub.png"),
        url: "sci-hub.shop/"
      },
      {
        title: "中华医学",
        image: require("../../../public/webIcon/中华医学期刊全文数据库.png"),
        url: "www.yiigle.com/index"
      },
      {
        title: "维普网",
        image: require("../../../public/webIcon/维普网.png"),
        url: "www.cqvip.com"
      },
    ]
)
const sort = ref(0)


const reKey = ref(0)
//搜索框绑定
const searchContent = ref([new SearchContent("default", true, "", null, false, store.state.LoginUser.name,sort.value,true)])

//搜索结果
const searchRes = ref([])

const addContent = () => {
  searchContent.value.push(new SearchContent("title", true, "", "AND", false, store.state.LoginUser.name,sort.value))
}

const afterSelect=(searchs) =>{
  searchs.precise=true
  if (searchs.type==="default"){
    searchs.type="title"
  }
}

const querySearch = async (queryString, cb, content) => {
  let HistoryItems = []
  if (queryString === "") {
    await HistoryService.getSearchHistory(new searchHistory(store.state.LoginUser.name, "", content.type)).then(res => {
          for (let i = 0; i < res.length; i++) {
            HistoryItems.push(new HistoryItem(res[i].content, ""))
          }
        }
    )
  } else {
    await HistoryService.getRemoteSearch(content).then(res => {
      console.log(res)
      if (content.type === "title" || content.type === "default") {
        for (let i = 0; i < res.length; i++) {
          HistoryItems.push(new HistoryItem(res[i].title, ""))
        }
      }else if(content.type === "authors"){
        for (let i = 0; i < res.length; i++) {
          HistoryItems.push(new HistoryItem(res[i].authors, ""))
        }
      }else if(content.type === "tags"){
        for (let i = 0; i < res.length; i++) {
          HistoryItems.push(new HistoryItem(res[i].tags, ""))
        }
      }else {
        HistoryItems.push(new HistoryItem("暂不支持文本远程搜索", ""))
      }
    })
  }
  cb(HistoryItems.reverse())
}

const firstSearch = async () => {
  console.log(searchContent)
  await SearchService.firstSearch(searchContent.value).then(res => {
        if (res.length === 0) {
          ElMessage({
            message: '未检索到任何内容',
            type: 'warning',
          })


        } else {
          globalVar.SearchContent = toRaw(searchContent.value)
          searchRes.value = res
          globalVar.FirstDocuments.value = toRaw(searchRes.value)
        }
      }
  )
  if (reKey.value === 0) {
    reKey.value++;
  }
}

const secondSearch = async (res) => {
  await jump.jumpToDetail(res, searchContent.value)
}

onMounted(
    () => {
      searchRes.value = globalVar.FirstDocuments.value
      if (globalVar.SearchContent !== null) {
        searchContent.value = globalVar.SearchContent
      }
    }
)

const isMobile = () => {
  return (window.screen.availWidth < 768)
}


</script>

<style scoped>
.font-color >>> .el-input__inner {
  color: #337ecc;
}
</style>