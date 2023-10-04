import {ref} from "vue";

//上传文件列表
let FileList = ref([])
//绑定的Upload Ref
let FileUpload = ref('')
//解析文件列表
let FileParseList = ref([])
//解析成功列表
let FileSuccessList = ref([])
//批量下载列表
let FileDownloadList = ref([])
//一级搜索结果
let FirstDocuments = ref([])
//搜索框内容
let SearchContent = null
//二级搜索结果
let SecondDocuments = ref([])
//本地历史记录
let localHistory=ref([])
//日历日期绑定
let Calendar=ref(new Date())

export default {
    FileList,
    FileUpload,
    FileParseList,
    FileSuccessList,
    FileDownloadList,
    FirstDocuments,
    SearchContent,
    SecondDocuments,
    localHistory,
    Calendar
}