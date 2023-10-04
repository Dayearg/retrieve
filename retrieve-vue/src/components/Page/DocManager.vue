<template>
  <UploadTable></UploadTable>
  <AnalyzeTable></AnalyzeTable>
  <SuccessTable></SuccessTable>
</template>

<script>
export default {
  name: "DocManager"
}
</script>

<script setup>
import UploadTable from "@/components/Table/UploadTable.vue";
import AnalyzeTable from "@/components/Table/AnalyzeTable.vue";
import SuccessTable from "@/components/Table/SuccessTable.vue";
</script>

<style scoped>

</style>