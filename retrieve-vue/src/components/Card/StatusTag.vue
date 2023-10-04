<template>
  <el-tooltip :content="statusClass.show" placement="bottom" effect="dark">
    <el-tag
        :type="statusClass.type"
    >{{ statusClass.value }}
    </el-tag>
  </el-tooltip>
</template>

<script>
export default {
  name: "StatusTag"
}
</script>

<script setup>
import {computed} from "vue";

const props = defineProps({
  statusInt: {
    type: Number,
    default: -1
  }
})
const statusClass = computed(() => {
  switch (props.statusInt) {
    case 0: {
      return {
        value: "成功",
        show: "所有用户均可检索",
        type: "success"
      }
    }
    case -1: {
      return {
        value: "未解析",
        type: "info"
      }
    }
    case -2: {
      return {
        value: "失败",
        type: "danger"
      }
    }
    case -3: {
      return {
        value: "失败",
        type: "danger"
      }
    }
    case 1: {
      return {
        value: "成功",
        show: "其他用户无法检索",
        type: "warning"
      }
    }
  }
})
</script>
<style scoped>

</style>