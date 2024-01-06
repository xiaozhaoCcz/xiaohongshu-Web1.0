<template>
  <div class="feeds-page" @scroll="handleScroll">
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
    <div class="feeds-container">
      <div class="feeds-loading-top" v-show="topLoading">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>

      <Waterfall
        :list="noteList"
        :width="options.width"
        :hasAroundGutter="options.hasAroundGutter"
        :animation-effect="options.animationEffect"
        :animation-duration="options.animationDuration"
        :animation-delay="options.animationDelay"
        :load-props="options.loadProps"
        :lazyload="options.lazyload"
        style="max-width: 1260px"
      >
        <template #item="{ item, url }">
          <div class="card">
            <LazyImg :url="url" @click="toMain(item.id)" class="fadeImg" />
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
    <Main v-show="mainShow" :nid="nid" class="mainShow" @click-main="close"></Main>
  </div>
</template>
<script lang="ts" setup>
import { RefreshRight, Top } from "@element-plus/icons-vue";
import { LazyImg, Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
// import { useRouter } from "vue-router";
import { ref, onMounted, reactive } from "vue";
import { getRecommendNotePage, getNotePageByDTO } from "@/api/search";
import { getCategoryTreeData } from "@/api/category";
import type { NoteSearch, NoteDTO } from "@/type/note";
import type { Category } from "@/type/category";
import Main from "@/pages/main.vue";
import loading from "@/assets/loading.png";
import error from "@/assets/error.png";
import FloatingBtn from "@/components/FloatingBtn";

// const router = useRouter();

const topLoading = ref(false);
const noteList = ref<Array<any>>([]);
const categoryList = ref<Array<Category>>([]);
const currentPage = ref(1);
const pageSize = ref(20);
const noteTotal = ref(0);
// const topBtnShow = ref(false);
const categoryClass = ref("0");
const mainShow = ref(false);
const nid = ref("");
const options = reactive({
  // 唯一key值
  rowKey: "id",
  // 卡片之间的间隙
  gutter: 10,
  // 是否有周围的gutter
  hasAroundGutter: false,
  // 卡片在PC上的宽度
  width: 240,
  // 自定义行显示个数，主要用于对移动端的适配
  breakpoints: {
    1200: {
      // 当屏幕宽度小于等于1200
      rowPerView: 4,
    },
    800: {
      // 当屏幕宽度小于等于800
      rowPerView: 3,
    },
    500: {
      // 当屏幕宽度小于等于500
      rowPerView: 2,
    },
  },
  // 动画效果
  animationEffect: "animate__fadeIn",
  // 动画时间
  animationDuration: 2000,
  // 动画延迟
  animationDelay: 1000,
  // 背景色
  backgroundColor: "#2C2E3A",
  // imgSelector
  imgSelector: "src.original",
  // 加载配置
  loadProps: {
    loading,
    error,
  },
  // 是否懒加载
  lazyload: true,
});
const queryParams = ref<NoteDTO>({
  keyword: "",
  type: 1,
  cid: "",
  cpid: "",
});

const toMain = (noteId: string) => {
  // console.log("11", nid);
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
};

const handleScroll = () => {
  const scrollHeight = Math.max(
    document.documentElement.scrollHeight,
    document.body.scrollHeight
  );
  //滚动条滚动距离
  const scrollTop =
    window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
  //窗口可视范围高度
  const clientHeight =
    window.innerHeight ||
    Math.min(document.documentElement.clientHeight, document.body.clientHeight);

  // topBtnShow.value = scrollTop > 30;
  if (clientHeight + scrollTop >= scrollHeight && currentPage.value <= noteTotal.value) {
    //快到底时----加载
    console.log("到达底部");
    loadMoreData();
  }
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
  if (queryParams.value.keyword == "" || queryParams.value.cid == "") {
    getRecommendNotePage(currentPage.value, pageSize.value).then((res: any) => {
      console.log("---res", res);
      setData(res);
    });
  } else {
    getNotePageByDTO(currentPage.value, pageSize.value, queryParams.value).then((res) => {
      setData(res);
    });
  }
};

const setData = (res: any) => {
  const { records, total } = res.data;
  noteTotal.value = total;
  const dataList = [] as Array<any>;
  records.forEach((item: any) => {
    const objData: NoteSearch = Object.assign(item, {});
    objData.src = item.noteCover;
    dataList.push(objData);
  });
  noteList.value.push(...dataList);
};

const getNoteList = () => {
  categoryClass.value = "0";
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getRecommendNotePage(currentPage.value, pageSize.value).then((res: any) => {
    console.log("---res", res);
    setData(res);
  });
};

const getNoteListByCategory = (id: string) => {
  categoryClass.value = id;
  queryParams.value.cpid = id;
  noteList.value = [] as Array<any>;
  currentPage.value = 1;
  getNotePageByDTO(currentPage.value, pageSize.value, queryParams.value).then((res) => {
    setData(res);
  });
};

const getCategoryData = () => {
  getCategoryTreeData().then((res: any) => {
    console.log("--category", res.data);
    categoryList.value = res.data;
  });
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

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

.fadeImg {
  border-radius: 8px;
  -webkit-animation: fadeinout 2s linear forwards;
  animation: fadeinout 2s linear forwards;
}

@-webkit-keyframes fadeinout {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 0;
  }
}

@keyframes fadeinout {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 0;
  }
}

@-webkit-keyframes fadeinout {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

@keyframes fadeinout {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

@-webkit-keyframes fadeinout {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

@keyframes fadeinout {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

.feeds-page {
  flex: 1;
  padding: 0 24px;
  padding-top: 72px;
  overflow: scroll;
  height: 100%;

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
