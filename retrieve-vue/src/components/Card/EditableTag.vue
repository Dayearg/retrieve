<template>
  <el-space wrap>
    <el-tag
        v-for="tag in list"
        :key="tag"
        class="mx-1"
        :closable="editable"
        :disable-transitions="false"
        @close="handleClose(tag)"
    >
      {{ tag }}
    </el-tag>
    <el-input
        v-if="inputVisible"
        ref="InputRef"
        v-model="inputValue"
        size="small"
        @keyup.enter="handleInputConfirm"
        @blur="handleInputConfirm"
        minlength="1"
        maxlength="10"
        style="width: 40px"
    />
    <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput" type="success" plain v-if="editable">
      +添加
    </el-button>
  </el-space>
</template>

<script>
export default {
  name: "EditableTag"
}
</script>

<script setup>
import {computed, onMounted, ref, toRefs, watch, watchEffect} from "vue";
import {nextTick} from 'vue'
import {ElInput} from 'element-plus'
import doc from "@/items/doc";

const inputValue = ref('')
const inputVisible = ref(false)
const InputRef = ref()

const labels = ['authors', 'tags']


const props = defineProps({
  documentRef: {
    type: doc,
    required: true
  },
  labelRef: {
    type: String,
    required: true
  }
})


const {documentRef, labelRef} = toRefs(props);
const getlistString = () => {
  if (labelRef.value === labels[0]) {
    return ref(documentRef.value.authors)
  } else if (labelRef.value === labels[1]) {
    return ref(documentRef.value.tags)
  }
  // 添加一个默认返回值，以免在其他情况下出现错误
  return ref("")
}

let listString = getlistString()
const editable = ref(documentRef.value.editable)


const getList = (str) => {
  if (str === "") {
    return []
  } else {
    if (str.charAt(str.length - 1) === ";") {
      str = str.substring(0, str.length - 1)
    }
    if (str.charAt(0) === ";") {
      str = str.substring(1, str.length)
    }
    return str.split(";")
  }
}

const list = ref(getList(listString.value))


const handleClose = (tag) => {
  listString.value = listString.value.replace(tag, "")
  listString.value = listString.value.replace(";;", ";")
  if (listString.value.charAt(listString.value.length - 1) === ";") {
    listString.value = listString.value.substring(0, listString.value.length - 1)
  }
  if (listString.value.charAt(0) === ";") {
    listString.value = listString.value.substring(1, listString.value.length)
  }
  if (labelRef.value === labels[0]) {
    documentRef.value.authors = listString.value
  } else if (labelRef.value === labels[1]) {
    documentRef.value.tags = listString.value
  }
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    InputRef.value.input.focus()
  })
}


//确认输入内容
const handleInputConfirm = () => {
  if (inputValue.value) {
    if (listString.value === "") {
      listString.value = inputValue.value
    } else {
      listString.value = listString.value + ";" + inputValue.value
      if (labelRef.value === labels[0]) {
        documentRef.value.authors = listString.value
      } else if (labelRef.value === labels[1]) {
        documentRef.value.tags = listString
      }
    }

  }
  inputVisible.value = false
  inputValue.value = ''
}


watchEffect(
    () => {
      listString = getlistString()
      list.value = getList(listString.value)
      editable.value = documentRef.value.editable
    }
)
</script>
<style scoped>

</style>