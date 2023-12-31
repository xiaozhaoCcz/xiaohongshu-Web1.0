<template>
  <div class="feeds-page" @scroll="handleScroll">
    <div class="channel-container">
      <div class="scroll-container channel-scroll-container">
        <div class="content-container">
          <div class="channel active">推荐</div>
          <div class="channel">穿搭</div>
          <div class="channel">美食</div>
          <div class="channel">彩妆</div>
          <div class="channel">影视</div>
          <div class="channel">职场</div>
          <div class="channel">情感</div>
          <div class="channel">家居</div>
          <div class="channel">游戏</div>
          <div class="channel">旅行</div>
          <div class="channel">动漫</div>
          <div class="channel">健身</div>
        </div>
      </div>
    </div>
    <div class="loading-container"></div>
    <div class="feeds-container">
      <div class="feeds-loading-top" v-show="topLoading">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>

      <Waterfall :list="noteList" :width="240" :hasAroundGutter="false" style="max-width: 1260px">
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
      <div class="feeds-loading" v-show="endLoading">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>
    </div>
    <div class="floating-btn-sets">
      <el-backtop :bottom="80" :right="24">
        <div class="back-top" v-show="topBtnShow">
          <Top style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
        </div>
      </el-backtop>

      <div class="reload" @click="refresh">
        <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
      </div>
    </div>
    <Main v-show="mainShow" :nid="nid" class="mainShow" @click-main="close"></Main>
  </div>
</template>
<script lang="ts" setup>
import { RefreshRight, Top } from "@element-plus/icons-vue";
import { LazyImg, Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
// import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { loadImageEnd } from "@/utils/util";
import { getRecommendNotePage } from "@/api/search";
import type { NoteSearch } from "@/type/note";
import Main from "@/pages/main.vue";

// const router = useRouter();

const topLoading = ref(false);
const endLoading = ref(true);
const noteList = ref<Array<any>>([]);
const currentPage = ref(1);
const pageSize = ref(20);
const noteTotal = ref(0);
const topBtnShow = ref(false);
const mainShow = ref(false);
const nid = ref("");

const toMain = (noteId: string) => {
  // console.log("11", nid);
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
};

const handleScroll = () => {
  const scrollHeight = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
  //滚动条滚动距离
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
  //窗口可视范围高度
  const clientHeight =
    window.innerHeight || Math.min(document.documentElement.clientHeight, document.body.clientHeight);

  topBtnShow.value = scrollTop > 30;
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
  let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
  const clientHeight =
    window.innerHeight || Math.min(document.documentElement.clientHeight, document.body.clientHeight);

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
  getNoteList();
};

const getNoteList = () => {
  const p = new Promise((reslove) => {
    getRecommendNotePage(currentPage.value, pageSize.value).then((res: any) => {
      console.log("---res", res);
      reslove(res.data);
    });
  });
  p.then((data: any) => {
    console.log("----data", data);
    const { records, total } = data;
    noteTotal.value = total;
    new Promise((resolve) => {
      const dataObj = {
        imgList: [] as Array<string>,
        dataList: [] as Array<any>,
      };
      records.forEach((item: any) => {
        dataObj.imgList.push(item.noteCover);
        dataObj.imgList.push(item.avatar);
        const objData: NoteSearch = Object.assign(item, {});
        objData.src = item.noteCover;
        dataObj.dataList.push(objData);
        resolve(dataObj);
      });
    }).then((data: any) => {
      console.log("---obj", data);
      loadImageEnd(
        data.imgList,
        () => {
          noteList.value.push(...data.dataList);
          endLoading.value = false;
        },
        false
      );
    });
  });
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

const initData = () => {
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
}
</style>
