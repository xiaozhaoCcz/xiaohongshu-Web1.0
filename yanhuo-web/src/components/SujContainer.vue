<template>
  <div class="sug-container-wrapper query-trending sug-pad">
    <div class="sug-container query-trending">
      <div class="history">
        <div class="header">
          <span> 历史记录 </span>
          <div class="icon-group">
            <div class="icon-box" @click="deleteRecord">
              <Delete style="width: 1.2em; height: 1.2em"></Delete>
            </div>
            <!---->
          </div>
        </div>
        <div class="history-list">
          <div v-for="(item, index) in historyRecordList" :key="index">
            <div class="history-item" @click="searchPage(item)">
              {{ item }}
              <!---->
            </div>
          </div>
          <!---->
        </div>
      </div>
      <div class="sug-box">
        <div class="header">猜你想搜</div>
        <div class="sug-wrapper">
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              斗破苍穹漫画
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              坂田银时
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              胖男生穿搭
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              朱竹清稀有图片
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              美杜莎全身
              <!---->
            </div>
            <!---->
          </div>
          <div class="sug-item query-trending query-trending hotspot">
            <div class="sug-text">
              男士冬季外套
              <!---->
            </div>
            <!---->
          </div>
        </div>
      </div>
      <div>
        <div class="hotspots">
          <div class="header">
            <img
              class="header-img"
              src="https://picasso-static.xiaohongshu.com/fe-platform/0c824dd79820e11ad8e3a5c86d56892d6159f0a8.png"
              crossorigin="anonymous"
            />
          </div>
          <div class="hotspot-list">
            <div class="hotspot-item" v-for="(item, index) in hotList" :key="index">
              <p class="hotspot-index">{{ index + 1 }}</p>
              <div class="hotspot-title" @click="searchPage(item.content)">
                <span class="text">{{ item.content }}</span>
                <img
                  src="https://sns-img-qc.xhscdn.com/search/trends/icon/label/new/version/1"
                  crossorigin="anonymous"
                />
              </div>
              <span class="hotspot-score">{{ item.searchCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Delete } from "@element-plus/icons-vue";
import { ref, onMounted, watch } from "vue";
import { useSearchStore } from "@/store/searchStore";
import { getHotRecord } from "@/api/search";
import { getRandomString } from "@/utils/util";

const searchStore = useSearchStore();

const historyRecordList = ref<Array<string>>([]);
const hotList = ref([]);

const deleteRecord = () => {
  console.log(123);
};

watch(
  () => [searchStore.seed],
  (newVal, oldVal) => {
    console.log("seed", newVal, oldVal);
    historyRecordList.value = searchStore.getRecords();
    getHotRecord().then((res) => {
      console.log(res.data);
      hotList.value = res.data;
    });
  }
);

const searchPage = (keyword: string) => {
  searchStore.setKeyword(keyword);
  searchStore.pushRecord(keyword);
  const seed = getRandomString(12);
  searchStore.setSeed(seed);
  console.log("-----seed", seed);
};

onMounted(() => {
  historyRecordList.value = searchStore.getRecords();
  getHotRecord().then((res) => {
    console.log(res.data);
    hotList.value = res.data;
  });
});
</script>
<style lang="less" scoped>
//隐藏滚动条
.sug-container-wrapper::-webkit-scrollbar {
  display: none;
}
.sug-container-wrapper.query-trending {
  position: relative;
  padding-top: 100%;
}
.sug-container-wrapper {
  margin-top: 8px;
  width: 100%;
  background-color: #fff;
  box-shadow: 0 4px 32px 0 rgba(0, 0, 0, 0.08), 0 1px 4px 0 rgba(0, 0, 0, 0.04);
  border-radius: 12px;
  overflow: scroll;
}

.sug-container.query-trending {
  width: 100%;
  position: absolute;
  top: 0;

  .history {
    padding: 4px;

    .header {
      display: flex;
      padding: 0 4px 0 12px;
      align-items: center;
      height: 32px;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 120%;
      color: rgba(51, 51, 51, 0.6);

      .icon-group {
        display: flex;
        margin-left: auto;
        font-size: 12px;
        font-weight: 400;
        color: rgba(51, 51, 51, 0.8);
        grid-gap: 4px;
        gap: 4px;
        .icon-box {
          display: flex;
          align-items: center;
          justify-content: center;
          height: 24px;
          width: 24px;
          grid-gap: 4px;
          gap: 4px;
          padding: 0 4px;
          cursor: pointer;
        }
      }
    }

    .history-list {
      display: flex;
      align-items: center;
      padding: 0 8px 8px;
      flex-wrap: wrap;
      position: relative;
      grid-gap: 8px;
      gap: 8px;

      .history-item {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 32px;
        color: rgba(51, 51, 51, 0.8);
        font-size: 14px;
        font-weight: 400;
        line-height: 120%;
        padding: 0 12px;
        white-space: nowrap;
        background: rgba(0, 0, 0, 0.03);
        border-radius: 999px;
        border: 1px solid transparent;
        cursor: pointer;
      }
    }
  }

  .sug-box {
    padding: 4px;

    .header {
      display: flex;
      padding: 10.5px 12px;
      align-items: center;
      height: 32px;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 120%;
      color: rgba(51, 51, 51, 0.6);
    }

    .sug-wrapper {
      display: flex;
      flex-wrap: wrap;

      .query-trending.hotspot:nth-child(odd) {
        margin-right: 2px;
      }

      .query-trending.hotspot {
        width: calc(50% - 2px);
      }

      .query-trending {
        color: rgba(51, 51, 51, 0.8);
        width: 100%;
      }

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

      .sug-text {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 20px;
      }
    }
  }

  .hotspots {
    padding: 4px;

    .header {
      padding: 8px 12px;
      height: 32px;
    }

    .hotspot-item:first-child {
      color: #ff2442;
    }
    .hotspot-item:nth-child(2) {
      color: rgb(128, 0, 94);
    }
    .hotspot-item:nth-child(3) {
      color: #ff24a4;
    }
    .hotspot-item {
      padding: 0 12px;
      display: flex;
      align-items: center;
      height: 40px;
      cursor: pointer;
      color: rgba(51, 51, 51, 0.6);

      .hotspot-index {
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 500;
        font-size: 14px;
        line-height: 120%;
        width: 16px;
        height: 16px;
      }

      .hotspot-title {
        margin: 0 6px;
        color: rgba(51, 51, 51, 0.8);
        font-weight: 400;
        font-size: 16px;
        line-height: 120%;
        display: flex;
        align-items: center;
        height: 100%;
        flex: 1;

        img {
          margin-left: 6px;
          height: 14px;
        }

        .text {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .hotspot-score {
        color: rgba(51, 51, 51, 0.3);
        margin-left: auto;
        font-weight: 400;
        font-size: 12px;
        line-height: 120%;
      }
    }
  }
}

.sug-container.query-trending {
  width: 100%;
  position: absolute;
  top: 0;
}
</style>
