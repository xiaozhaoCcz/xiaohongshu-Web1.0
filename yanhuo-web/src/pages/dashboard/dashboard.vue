<template>
  <div class="feeds-page">
    <div class="channel-container">
      <div class="scroll-container channel-scroll-container">
        <div class="content-container">
          <div
            :class="categoryClass == '0' ? 'channel active' : 'channel'"
            @click="getNoteList"
          >
            推荐
          </div>
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
    <div class="feeds-container" v-infinite-scroll="loadMoreData">
      <div class="feeds-loading-top" v-show="topLoading">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>

      <Waterfall
        :list="noteList"
        :width="options.width"
        :gutter="options.gutter"
        :hasAroundGutter="options.hasAroundGutter"
        :animation-effect="options.animationEffect"
        :animation-duration="options.animationDuration"
        :animation-delay="options.animationDelay"
        :load-props="options.loadProps"
        :lazyload="options.lazyload"
        style="max-width: 1260px"
      >
        <template #item="{ item }">
          <div class="card" style="width: 240px">
            <el-image class="noteImg" @click="toMain(item.id)" :src="item.noteCover">
              <template #error>
                <div class="image-slot">
                  <el-icon><icon-picture /></el-icon>
                </div>
              </template>
            </el-image>
            <!-- <LazyImg :url="url" @click="toMain(item.id)" style="width: 240px; max-height: 300px; object-fit: cover"/> -->
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
      </Waterfall>
      <div class="feeds-loading">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>
    </div>
    <FloatingBtn @click-refresh="refresh"></FloatingBtn>
    <Main
      v-show="mainShow"
      :nid="nid"
      class="animate__animated animate__zoomIn animate__delay-0.5s"
      @click-main="close"
    ></Main>
  </div>
</template>
<script lang="ts" setup>
import { RefreshRight } from "@element-plus/icons-vue";
import { Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
// import { useRouter } from "vue-router";
import { ref, watch } from "vue";
import { getRecommendNotePage, getNotePageByDTO, addRecord } from "@/api/search";
import { getCategoryTreeData } from "@/api/category";
import type { NoteDTO } from "@/type/note";
import type { Category } from "@/type/category";
import Main from "@/pages/main/main.vue";
import FloatingBtn from "@/components/FloatingBtn";
import { options } from "@/constant/constant";
import { useSearchStore } from "@/store/searchStore";
const searchStore = useSearchStore();

// const router = useRouter();

const topLoading = ref(false);
const noteList = ref<Array<any>>([]);
const categoryList = ref<Array<Category>>([]);
const currentPage = ref(1);
const pageSize = 20;
const noteTotal = ref(0);
// const topBtnShow = ref(false);
const categoryClass = ref("0");
const mainShow = ref(false);
const nid = ref("");
const queryParams = ref<NoteDTO>({
  keyword: "",
  type: 1,
  cid: "",
  cpid: "",
});

watch(
  () => [searchStore.seed],
  (newVal, oldVal) => {
    console.log("dashboardseed", newVal, oldVal);
    queryParams.value.keyword = searchStore.keyWord;
    queryParams.value.cpid = "";
    categoryClass.value = "0";
    getNoteListByKeyword();
    addRecord(searchStore.keyWord);
  }
);

const toMain = (noteId: string) => {
  // console.log("11", nid);
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
};

const close = () => {
  mainShow.value = false;
};

const refresh = () => {
  console.log("刷新数据");
  let scrollTop =
    window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
  const clientHeight =
    window.innerHeight ||
    Math.min(document.documentElement.clientHeight, document.body.clientHeight);

  console.log(scrollTop, "scrollTop");
  if (scrollTop <= clientHeight * 2) {
    const timeTop = setInterval(() => {
      document.documentElement.scrollTop = document.body.scrollTop = scrollTop -= 100;
      if (scrollTop <= 0) {
        clearInterval(timeTop);
        topLoading.value = true;
        setTimeout(() => {
          currentPage.value = 1;
          noteList.value = [];
          getNoteList();
          topLoading.value = false;
        }, 1000);
      }
    }, 10); //定时调用函数使其更顺滑
  } else {
    document.documentElement.scrollTop = 0;
    topLoading.value = true;
    setTimeout(() => {
      currentPage.value = 1;
      noteList.value = [];
      getNoteList();
      topLoading.value = false;
    }, 1000);
  }
};

const loadMoreData = () => {
  currentPage.value += 1;
  if (queryParams.value.cpid === "" && queryParams.value.keyword == "") {
    console.log("-----getRecommendNotePage", queryParams.value.keyword);
    getRecommendNotePage(currentPage.value, pageSize).then((res: any) => {
      console.log("---res", res);
      setData(res);
    });
  } else {
    console.log("-----getNotePageByDTO");
    getNotePageByDTO(currentPage.value, pageSize, queryParams.value).then((res) => {
      setData(res);
    });
  }
};

const setData = (res: any) => {
  const { records, total } = res.data;
  noteTotal.value = total;
  noteList.value.push(...records);
};

const getNoteList = () => {
  categoryClass.value = "0";
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getRecommendNotePage(currentPage.value, pageSize).then((res: any) => {
    console.log("---res", res);
    setData(res);
  });
};

const getNoteListByCategory = (id: string) => {
  categoryClass.value = id;
  queryParams.value.cpid = id;
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getNotePageByDTO(currentPage.value, pageSize, queryParams.value).then((res) => {
    setData(res);
  });
};

const getNoteListByKeyword = () => {
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getNotePageByDTO(currentPage.value, pageSize, queryParams.value).then((res) => {
    setData(res);
  });
};

const getCategoryData = () => {
  getCategoryTreeData().then((res: any) => {
    console.log("--category", res.data);
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
.mainShow {
  -webkit-animation: zoom_1 0.5s;
}
@-webkit-keyframes zoom_1 {
  0% {
    -webkit-transform: scale(0);
    opacity: 0;
  }
}

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

    .feeds-loading-top {
      -webkit-animation: move_1 0.5s;
    }
    @-webkit-keyframes move_1 {
      0% {
        -webkit-transform: translateY(-20px);
        opacity: 0;
      }
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
      box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.02);
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
