<template>
  <div id="secondCharts" style="width: 100%; height: 300px;"></div>
</template>
<script setup>
import * as echarts from "echarts";
import {onMounted, ref} from "vue";
import globalVar from "@/model/globalVar";

const chart = ref(null);
const chartData = ref([]);

// 计算分数段
function calculateScoreRanges(searchResults) {
  const scores = searchResults.map((result) => result.score);
  const maxScore = Math.max(...scores);
  const minScore = Math.min(...scores);
  const rangeSize = (maxScore - minScore) / 3;
  const scoreRanges = [
    {name: "高", minScore: maxScore - rangeSize},
    {name: "中", minScore: minScore + rangeSize},
    {name: "低", minScore: minScore},
  ];

  chartData.value = scoreRanges.map((range) => ({
    name: range.name,
    value: searchResults.filter(
        (result) => result.score >= range.minScore && result.score < range.minScore + rangeSize
    ).length,
  }));
}

onMounted(() => {
  // 初始化图表
  chart.value = echarts.init(document.getElementById("secondCharts"));

  console.log("更新数据")
  console.log(globalVar.SecondDocuments.value)
  // 计算分数段
  calculateScoreRanges(globalVar.SecondDocuments.value);

  console.log("图表数据")
  console.log(chartData.value)

  // 绘制图表
  chart.value.setOption({
    title: {
      text: "当前文献段落分数统计",
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
      data: chartData.value.map((data) => data.name),
      axisTick: {
        alignWithLabel: true,
      },
    },
    series: [
      {
        name: "分数段数量",
        type: "bar",
        barWidth: "60%",
        data: chartData.value.map((data) => data.value),
        color: "#409EFF",
      },
    ],
  });
});
</script>
<script>
export default {
  name: "SecondDistribution",
};
</script>
<style scoped>
</style>