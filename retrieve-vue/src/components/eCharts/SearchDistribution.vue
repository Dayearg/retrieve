<template>
  <div id="scoreChart" style="width: 100%; height: 300px;"></div>
</template>

<script setup>
import {onMounted, watchEffect} from "vue";
import globalVar from "@/model/globalVar";
import * as echarts from "echarts";

let chartData = [];


// 配置项
const option = {
  title: {
    text: "搜索结果分数数量统计",
    left: "center",
  },
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "shadow",
    },
  },
  xAxis: {
    type: "value",
  },
  yAxis: {
    type: "category",
    data: chartData.map((item) => item.name),
    axisTick: {
      alignWithLabel: true,
    },
    axisLabel: {
      interval: 0,
      rotate: 90,
    },
  },
  series: [
    {
      name: "分数段数量",
      type: "bar",
      barWidth: "60%",
      data: chartData.map((item) => item.value),
      color: "#409EFF",
    },
  ],
};

// 数据处理函数
function processData() {
  const maxScore = Math.max(
      ...globalVar.FirstDocuments.value.map((item) => item.score)
  );
  const minScore = Math.min(
      ...globalVar.FirstDocuments.value.map((item) => item.score)
  );
  const interval = (maxScore - minScore) / 5;
  const count = [0, 0, 0, 0, 0];
  const scoreCounts = globalVar.FirstDocuments.value.reduce((acc, cur) => {
    const index = Math.floor((cur.score - minScore) / interval);
    acc[index]++;
    return acc;
  }, count);
  const chartData = [
    {name: `高`, value: scoreCounts[0]},
    {name: `较高`, value: scoreCounts[1]},
    {name: `中等`, value: scoreCounts[2]},
    {name: `低`, value: scoreCounts[3]},
    {name: `极低`, value: scoreCounts[4]},
  ];
  return {chartData};
}

onMounted(() => {
  const chart = echarts.init(document.getElementById("scoreChart"));
  watchEffect(() => {
    const {chartData} = processData();
    option.yAxis.data = chartData.map((item) => item.name);
    option.series[0].data = chartData.map((item) => item.value);
    chart.setOption(option);
  });
});


</script>

<style scoped>
</style>
