<template>
  <div class="sug-container-wrapper sug-pad">
    <div class="sug-container">
      <!---->
      <div class="sug-box">
        <!---->
        <div class="sug-wrapper">
          <div class="sug-item" v-for="(item, index) in dataList" :key="index" @click="searchPage(item.content)">
            <!---->
            <span v-html="item.highlightContent"></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import { getRandomString } from "@/utils/util";
import { useSearchStore } from "@/store/searchStore";

const searchStore = useSearchStore();
const props = defineProps({
  recordList: {
    type: Array<any>,
    default: [],
  },
});

const dataList = ref<Array<any>>([]);

const searchPage = (keyword: string) => {
  searchStore.setKeyword(keyword);
  searchStore.pushRecord(keyword);
  const seed = getRandomString(12);
  searchStore.setSeed(seed);
};
watchEffect(() => {
  dataList.value = [];
  if (props.recordList.length > 0) {
    dataList.value = props.recordList;
  }
});
</script>
<style lang="less" scoped>
.sug-container-wrapper::-webkit-scrollbar {
  display: none;
}

.sug-container-wrapper {
  margin-top: 8px;
  width: 100%;
  background-color: #fff;
  box-shadow:
    0 4px 32px 0 rgba(0, 0, 0, 0.08),
    0 1px 4px 0 rgba(0, 0, 0, 0.04);
  border-radius: 12px;
  overflow: scroll;
  z-index: 9999;

  .sug-container {
    padding-top: 4px;

    .sug-item {
      width: 100%;
      padding: 0 12px;
      font-size: 16px;
      height: 40px;
      line-height: 120%;
      font-weight: 400;
      border-radius: 8px;
      color: rgba(51, 51, 51, 0.6);
      display: flex;
      align-items: center;
    }

    .sug-item:hover {
      background: #f8f8f8;
    }
  }
}
</style>
