<template>
  <div class="feeds-page">
    <div class="channel-container">
      <div class="scroll-container channel-scroll-container">
        <div class="content-container">
          <div :class="categoryClass == '0' ? 'channel active' : 'channel'" @click="getNoteList">推荐</div>
          <div
            :class="categoryClass == item.id ? 'channel active' : 'channel'"
            v-for="item in categoryList"
            :key="item.id"
            @click="getNoteListByCategory(item.id)"
          >
            {{ item.title }}
          </div>
        </div>
      </div>
    </div>
    <div class="loading-container"></div>
    <div class="feeds-container" v-infinite-scroll="loadMoreData" :infinite-scroll-distance="50">
      <div class="feeds-loading-top animate__animated animate__zoomIn animate__delay-0.5s" v-show="topLoading">
        <Loading style="width: 1.2em; height: 1.2em"></Loading>
      </div>

      <Waterfall
        :list="noteList"
        :width="options.width"
        :gutter="options.gutter"
        :hasAroundGutter="options.hasAroundGutter"
        :animation-effect="options.animationEffect"
        :animation-duration="options.animationDuration"
        :animation-delay="options.animationDelay"
        :breakpoints="options.breakpoints"
        style="min-width: 740px"
      >
        <template #item="{ item }">
          <el-skeleton style="width: 240px" :loading="!item.isLoading" animated>
            <template #template>
              <el-image
                :src="item.noteCover"
                :style="{
                  width: '240px',
                  maxHeight: '300px',
                  height: item.noteCoverHeight + 'px',
                  borderRadius: '8px',
                }"
                @load="handleLoad(item)"
              ></el-image>
              <div style="padding: 14px">
                <el-skeleton-item variant="h3" style="width: 100%" />
                <div style="display: flex; align-items: center; margin-top: 2px; height: 16px">
                  <el-skeleton style="--el-skeleton-circle-size: 20px">
                    <template #template>
                      <el-skeleton-item variant="circle" />
                    </template>
                  </el-skeleton>
                  <el-skeleton-item variant="text" style="margin-left: 10px" />
                </div>
              </div>
            </template>

            <template #default>
              <div class="card" style="max-width: 240px">
                <el-image
                  :src="item.noteCover"
                  :style="{
                    width: '240px',
                    maxHeight: '300px',
                    height: item.noteCoverHeight + 'px',
                    borderRadius: '8px',
                  }"
                  fit="cover"
                  @click="toMain(item.id)"
                ></el-image>
                <div class="footer">
                  <a class="title">
                    <span>{{ item.title }}</span>
                  </a>
                  <div class="author-wrapper">
                    <a class="author">
                      <img class="author-avatar" :src="item.avatar" />
                      <span class="name">{{ item.username }}</span>
                    </a>
                    <span class="like-wrapper like-active">
                      <i class="iconfont icon-follow" style="width: 1em; height: 1em"></i>
                      <span class="count">{{ item.likeCount }}</span>
                    </span>
                  </div>
                </div>
              </div>
            </template>
          </el-skeleton>
        </template>
      </Waterfall>

      <div class="feeds-loading">
        <!-- <Refresh style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" /> -->
        <Loading style="width: 1.2em; height: 1.2em"></Loading>
      </div>
    </div>
    <FloatingBtn @click-refresh="refresh"></FloatingBtn>
    <Main
      v-show="mainShow"
      :nid="nid"
      :nowTime="new Date()"
      class="animate__animated animate__zoomIn animate__delay-0.5s"
      @click-main="close"
    ></Main>
  </div>
</template>

<script lang="ts" setup>
import { Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
import { ref, watch } from "vue";
import { getRecommendNotePage, getNotePageByDTO, addRecord } from "@/api/search";
import { getCategoryTreeData } from "@/api/category";
import type { NoteDTO, NoteSearch } from "@/type/note";
import type { Category } from "@/type/category";
import Main from "@/pages/main/main.vue";
import FloatingBtn from "@/components/FloatingBtn.vue";
import { options } from "@/constant/constant";
import { useSearchStore } from "@/store/searchStore";
import Loading from "@/components/Loading.vue";
import { refreshTab } from "@/utils/util";
const searchStore = useSearchStore();
const topLoading = ref(false);
const noteList = ref<Array<NoteSearch>>([]);
const categoryList = ref<Array<Category>>([]);
const currentPage = ref(1);
const pageSize = 20;
const noteTotal = ref(0);
const categoryClass = ref("0");
const mainShow = ref(false);
const nid = ref("");
const noteDTO = ref<NoteDTO>({
  keyword: "",
  type: 0,
  cid: "",
  cpid: "",
});

watch(
  () => [searchStore.seed],
  () => {
    noteDTO.value.keyword = searchStore.keyWord;
    noteDTO.value.cpid = "";
    categoryClass.value = "0";
    getNoteListByKeyword();
    addRecord(searchStore.keyWord);
  }
);

const toMain = (noteId: string) => {
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
};

const close = () => {
  mainShow.value = false;
};

const handleLoad = (item: any) => {
  item.isLoading = true;
};

const refresh = () => {
  // 使用回调函数优化代码
  refreshTab(() => {
    topLoading.value = true;
    console.log(111);
    setTimeout(() => {
      currentPage.value = 1;
      noteList.value = [];
      getNoteList();
      topLoading.value = false;
    }, 1000);
  });
};

const loadMoreData = () => {
  console.log("-----loadmore");
  currentPage.value += 1;
  if (noteDTO.value.cpid === "" && noteDTO.value.keyword == "") {
    getRecommendNotePage(currentPage.value, pageSize).then((res: any) => {
      setData(res);
    });
  } else {
    getNotePageByDTO(currentPage.value, pageSize, noteDTO.value).then((res) => {
      setData(res);
    });
  }
};

const setData = (res: any) => {
  const { records, total } = res.data;
  console.log(records, total);
  noteTotal.value = total;
  noteList.value.push(...records);
};

const getNoteList = async () => {
  categoryClass.value = "0";
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getRecommendNotePage(currentPage.value, pageSize).then((res: any) => {
    setData(res);
  });
};

const getNoteListByCategory = (id: string) => {
  categoryClass.value = id;
  noteDTO.value.cpid = id;
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getNotePageByDTO(currentPage.value, pageSize, noteDTO.value).then((res) => {
    setData(res);
  });
};

const getNoteListByKeyword = () => {
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getNotePageByDTO(currentPage.value, pageSize, noteDTO.value).then((res) => {
    setData(res);
  });
};

const getCategoryData = () => {
  getCategoryTreeData().then((res: any) => {
    categoryList.value = res.data;
  });
};

const initData = () => {
  getCategoryData();
  getNoteList();
};

initData();
</script>

<style lang="less" scoped>
.feeds-page {
  flex: 1;
  padding: 0 24px;
  padding-top: 72px;
  height: 100vh;

  .channel-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    user-select: none;
    -webkit-user-select: none;

    .channel-scroll-container {
      backdrop-filter: blur(20px);
      background-color: transparent;
      width: calc(100vw - 24px);

      position: relative;
      overflow: hidden;
      display: flex;
      user-select: none;
      -webkit-user-select: none;
      align-items: center;
      font-size: 16px;
      color: rgba(51, 51, 51, 0.8);
      height: 40px;
      white-space: nowrap;
      height: 72px;

      .content-container::-webkit-scrollbar {
        display: none;
      }

      .content-container {
        display: flex;
        overflow-x: scroll;
        overflow-y: hidden;
        white-space: nowrap;
        color: rgba(51, 51, 51, 0.8);

        .active {
          font-weight: 600;
          background: rgba(0, 0, 0, 0.03);
          border-radius: 999px;
          color: #333;
        }

        .channel {
          height: 40px;
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 0 16px;
          cursor: pointer;
          -webkit-user-select: none;
          user-select: none;
        }
      }
    }
  }

  .feeds-container {
    position: relative;
    transition: width 0.5s;
    margin: 0 auto;

    .feeds-loading {
      margin: 3vh;
      text-align: center;
    }

    .feeds-loading-top {
      text-align: center;
      line-height: 6vh;
      height: 6vh;
    }

    .noteImg {
      width: 240px;
      max-height: 300px;
      object-fit: cover;
      border-radius: 8px;
    }

    .footer {
      padding: 12px;

      .title {
        margin-bottom: 8px;
        word-break: break-all;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        font-weight: 500;
        font-size: 14px;
        line-height: 140%;
        color: #333;
      }

      .author-wrapper {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 20px;
        color: rgba(51, 51, 51, 0.8);
        font-size: 12px;
        transition: color 1s;

        .author {
          display: flex;
          align-items: center;
          color: inherit;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-right: 12px;

          .author-avatar {
            margin-right: 6px;
            width: 20px;
            height: 20px;
            border-radius: 20px;
            border: 1px solid rgba(0, 0, 0, 0.08);
            flex-shrink: 0;
            object-fit: cover;
          }

          .name {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        .like-wrapper {
          position: relative;
          cursor: pointer;
          display: flex;
          align-items: center;

          .count {
            margin-left: 2px;
          }
        }
      }
    }
  }

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
      // transition: background 0.2s;
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
      //transition: background 0.2s;
      cursor: pointer;
    }
  }
}
</style>
