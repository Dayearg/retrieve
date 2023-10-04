<template>
  <el-card :body-style="{ padding: '0px'}" style="max-width: 1000px;width: 100%">
    <el-row :gutter="20" justify="space-between">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" v-if="searchRes.source===0">
        <div style="margin: 15px">
          <el-space direction="horizontal"
                    fill-ratio="100"
                    fill="fill"
          >
            <el-row :gutter="20">
              <el-space
                  direction="horizontal" wrap>
                <el-col>
                  <el-tag class="mx-1" size="default" effect="dark" type="success">第{{ searchRes.page }}页</el-tag>
                </el-col>
                <el-col>
                  <el-tag class="mx-1" size="default" effect="dark">来源：{{ sourceString(searchRes) }}</el-tag>
                </el-col>
              </el-space>
            </el-row>
            <el-row>
              <div v-html="searchRes.text"></div>
            </el-row>
          </el-space>
        </div>
      </el-col>
      <el-col :xs="24" :sm="20" :md="20" :lg="20" :xl="20" v-if="searchRes.source===1">
        <div style="margin: 15px">
          <el-space direction="horizontal"
                    fill-ratio="100"
                    fill="fill"
          >
            <el-row :gutter="20">
              <el-space
                  direction="horizontal" wrap>
                <el-col>
                  <el-tag class="mx-1" size="default" effect="dark" type="success">第{{ searchRes.page }}页</el-tag>
                </el-col>
                <el-col>
                  <el-tag class="mx-1" size="default" effect="dark" type="warning">来源：{{
                      sourceString(searchRes)
                    }}
                  </el-tag>
                </el-col>
              </el-space>
            </el-row>
            <el-row>
              <div v-html="searchRes.text"></div>
            </el-row>
          </el-space>
        </div>
      </el-col>
      <el-col :xs="24" :sm="4" :md="4" :lg="4" :xl="4" v-if="searchRes.source===1">
        <img :src="'http://'+searchRes.imgUrl" class="image"/>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  name: "DetailCard"
}
</script>

<script setup>
import secondSearch from "@/items/secondSearch";
import {toRaw} from "vue";

const props = defineProps(
    {
      searchRes: {
        type: secondSearch,
        required: true
      }
    }
)

function sourceString(searchRes) {
  if (toRaw(searchRes).source === 0)
    return "文本"
  else
    return "图片"
}
</script>

<style scoped>
.image {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: fill;
}
</style>