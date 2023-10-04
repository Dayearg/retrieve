<template>
  <div id="totalCharts" style="height: 100%;width: 100%"></div>
</template>
<script setup>
import {onMounted, ref} from "vue";
import ChartsService from "@/services/ChartsService";
import chartXy from "@/items/chartXy";
import * as echarts from "echarts";

const data = ref();

const getData = async () => {
  await ChartsService.getTotal().then((res) => {
    console.log(res)
    data.value = new chartXy(Object.keys(res), Object.values(res));
    const option = {
      title: {
        text: "总文件数量变化趋势",
        left: "center",
      },
      tooltip: {
        trigger: "axis",
      },
      xAxis: {
        type: "category",
        data: data.value.xLabel,
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          type: "line",
          smooth: true,
          data: data.value.yValue,
          color: "#409EFF",
        },
      ],
    };
    console.log(data.value)
    const chart = echarts.init(document.getElementById("totalCharts"));
    chart.setOption(option);
  });
};

// 在组件挂载时创建图表
onMounted(() => {
  getData();
});
</script>
<style scoped>
</style>