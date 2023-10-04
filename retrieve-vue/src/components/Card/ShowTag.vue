<template>
  <el-space wrap>
    <el-tag
        v-for="tag in list"
        :key="tag"
        :disable-transitions="false"
        size="small"
    >
      {{ tag }}
    </el-tag>
  </el-space>
</template>

<script>
export default {
  name: "showTag"
}
</script>

<script setup>

import {computed, ref} from "vue";

const props = defineProps({
  listString: {
    required: true
  },
  show: {
    type: Number,
    required: true
  }
})


const listString = ref(props.listString)
const list = computed(() => {
  // use listString.value instead of listString
  if (listString.value === "") {
    return []
  } else {
    if (listString.value.charAt(listString.value.length - 1) === ";") {
      listString.value = listString.value.substring(0, listString.value.length - 1)
    }
    return listString.value.split(";").slice(0, props.show)
  }
})

</script>

<style scoped>

</style>