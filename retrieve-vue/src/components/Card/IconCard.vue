<template>
  <el-card :body-style="{ padding: '0px' }" style="max-width: 50px;max-height: 50px;" @click="newPage(item.url)">
    <img
        style="object-fit:fill;width: 50px;height: 50px;"
        :src="item.image"
    />
  </el-card>
  <div style="margin-bottom: 5px;margin-top: 3px;max-width: 50px;font-size: 1.2ch;color: gray" align="center">
    <span>{{ item.title }}</span>
  </div>


</template>

<script>
export default {
  name: "IconCard"
}
</script>

<script setup>
import newPage from "@/routers/newPage";

const props = defineProps({
  item: {
    required: true,
  },
})

</script>
<style scoped>

</style>