<template>
  <el-card shadow="always" style="max-width: 1000px;width: 100%">
    <!--    标题与详细信息的间距，自动填充-->
    <el-space direction="horizontal"
              fill-ratio="100"
              fill="fill">
      <!--      标题行-->
      <el-row justify="start" align="middle">
        <el-space wrap>
          <span>{{ searchRes.title }}</span>
          <ShowTag :key="searchRes.tags" :show="3" :list-string="searchRes.tags"/>
        </el-space>
      </el-row>
      <!--      详细信息行-->
      <el-row gutter="20" justify="space-between" align="middle">

        <!--        元信息列：上传时间、上传用户、下载次数-->
        <el-col :span="16" class="title-font">

          <el-row gutter="20" justify="space-between">
            <!--              默认水平排列，空间不够时折叠-->
            <el-space
                direction="horizontal" wrap>
              <el-col>
                <el-row :gutter="20" justify="start">
                  <el-col style="white-space: nowrap">
                    <el-space wrap>
                      <el-tag type="info" size="small">上传时间</el-tag>
                    </el-space>
                    <el-tag size="small">
                      {{ searchRes.uploadTime }}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>

              <el-col>
                <el-row :gutter="20" justify="start">
                  <el-col style="white-space: nowrap">
                    <el-space wrap>
                      <el-tag type="info" size="small">作者</el-tag>
                      <ShowTag :key="searchRes.authors" show="2" :list-string="searchRes.authors"/>
                    </el-space>
                  </el-col>
                </el-row>
              </el-col>

              <el-col>
                <el-row :gutter="20" justify="start">
                  <el-col style="white-space: nowrap">
                    <el-space wrap>
                      <el-tag type="info" size="small">下载次数</el-tag>
                    </el-space>
                    <el-tag size="small">
                      {{ searchRes.download }}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>

              <el-col>
                <el-row :gutter="20" justify="start">
                  <el-col style="white-space: nowrap">
                    <el-space wrap>
                      <el-tag type="info" size="small">文档页数</el-tag>
                    </el-space>
                    <el-tag size="small">
                      {{ searchRes.totalPages }}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>

            </el-space>
          </el-row>
        </el-col>
        <!--        检索信息列-->
        <el-col :span="6">
          <el-row justify="end">
            <el-space
                wrap
                direction="horizontal">
              <el-col>
                <el-tag class="mx-1" size="default" effect="dark" type="success">得分：{{ parseInt(searchRes.score) }}
                </el-tag>
              </el-col>
              <el-col>
                <el-tag class="mx-1" size="default" effect="dark">命中：{{ searchRes.targetTimes }}</el-tag>
              </el-col>
            </el-space>
          </el-row>

        </el-col>
      </el-row>

    </el-space>
  </el-card>
</template>

<script>
export default {
  name: "ResCard"
}
</script>

<script setup>

import FirstSearch from "@/items/firstSearch";
import ShowTag from "@/components/Card/ShowTag.vue";
import {onMounted} from "vue";

const props = defineProps({
  searchRes: {
    type: FirstSearch,
    required: true
  }
})


</script>

<style scoped>
.title-font {
  font-family: "Hiragino Sans GB";
  font-weight: bold;
  font-size: small;
}

</style>