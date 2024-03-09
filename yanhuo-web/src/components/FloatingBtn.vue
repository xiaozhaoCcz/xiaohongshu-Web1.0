<template>
  <div class="floating-btn-sets">
    <el-backtop :bottom="80" :right="24">
      <div class="back-top">
        <Top style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>
    </el-backtop>

    <div class="reload" @click="refresh">
      <Refresh style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Refresh, Top } from "@element-plus/icons-vue";

const emit = defineEmits(["clickRefresh"]);
const refresh = () => {
  emit("clickRefresh", true);
};
</script>
<style lang="less" scoped>
.floating-btn-sets {
  position: fixed;
  display: flex;
  flex-direction: column;
  width: 40px;
  grid-gap: 8px;
  gap: 8px;
  right: 24px;
  bottom: 24px;

  .back-top {
    width: 40px;
    height: 40px;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, 0.08);
    border-radius: 100px;
    color: rgba(51, 51, 51, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.2s;
    cursor: pointer;
  }

  .reload {
    width: 40px;
    height: 40px;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, 0.08);
    box-shadow:
      0 2px 8px 0 rgba(0, 0, 0, 0.1),
      0 1px 2px 0 rgba(0, 0, 0, 0.02);
    border-radius: 100px;
    color: rgba(51, 51, 51, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.2s;
    cursor: pointer;
  }
}
</style>
