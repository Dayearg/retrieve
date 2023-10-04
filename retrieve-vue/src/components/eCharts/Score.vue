<template>
<div id="score" style="width: 100%; height: 100%;min-height: 150px"></div>
</template>


<script setup>
import {onMounted, ref} from "vue";
import * as echarts from "echarts";

const props = defineProps({
  firstDocument: {
    type: Object,
    required: true
  }
})

const socre=ref(props.firstDocument.score)
const click=ref(props.firstDocument.click)
const collect=ref(props.firstDocument.collect)
const download=ref(props.firstDocument.download)

const data=ref()

const option = {
    grid:{
    left:"2%",
    bottom:"2%"
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    left: 'center',
    top: 'bottom',
    data: [
      '词频',
        '点击',
        '收藏',
        '下载'
    ]
  },
  toolbox: {
    show: false,
    feature: {
      mark: { show: true },
      dataView: { show: true, readOnly: false },
      restore: { show: true },
      saveAsImage: { show: true }
    }
  },
  series: [
    {
      name: '详情',
      type: 'pie',
      radius: '70%',  //图的大小
      center: ['50%', '50%'],
      roseType: 'area',
      itemStyle: {
        borderRadius: 5
      },
      label: {
        show: false
      },
      emphasis: {
        label: {
          show: true
        }
      },
      data: [
        { value: socre.value, name: '词频' },
        { value: click.value, name: '点击' },
        { value: collect.value*50, name: '收藏' },
        { value: download.value*10, name: '下载' },
      ]
    },
  ]
};

onMounted(
    ()=>{
      const chart = echarts.init(document.getElementById("score"),null,{
      });
      chart.setOption(option);
    }
)

</script>

<style scoped>

</style>