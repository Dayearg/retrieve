<template>
  <div v-for="item in result">
    <p style="margin: 5px;font-size: 20px;color: cornflowerblue">
      {{ item[0].date }}
    </p>

    <el-table :data="item" stripe style="width: 100%">
      <el-table-column prop="title" label="标题" min-width="80%"/>
      <el-table-column prop="date" label="日期" min-width="20%"/>
    </el-table>
  </div>
</template>


<script>
export default {
  name: "History",

}

</script>
<script setup>
import {onMounted, ref, watch} from "vue";

import HistoryService from "@/services/HistoryService";
import {useStore} from "vuex";
import globalVar from "@/model/globalVar";

const result = ref([])
const store = useStore()




const getResult = async () => {
  await HistoryService.getBrowsingHistory(store.state.LoginUser.name).then(res => {
    result.value = groupByDate(res)
  })
}

const groupByDate = (browsingHistoryArray) => {
  const groupedArray = browsingHistoryArray.reduce((result, history) => {
    const {date} = history;
    const existingGroup = result.find(group => group[0].date === date);

    if (existingGroup) {
      existingGroup.push(history);
    } else {
      result.push([history]);
    }

    return result;
  }, []);

  // 根据 date 属性进行排序
  groupedArray.sort((a, b) => {
    const dateA = new Date(a[0].date);
    const dateB = new Date(b[0].date);
    return dateB - dateA;
  });
  return groupedArray;
}

onMounted(
    () => {
      getResult()
    }
)

watch(globalVar.Calendar, () => {
      console.log(globalVar.Calendar.value.toLocaleDateString())
      getResult()
    }
)
</script>


<style scoped>

</style>