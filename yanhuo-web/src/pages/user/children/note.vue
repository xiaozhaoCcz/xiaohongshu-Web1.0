<template>
  <div class="feeds-container">
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
    <Main v-show="mainShow" :nid="nid" class="mainShow" @click-main="close"></Main>
  </div>
</template>
<script lang="ts" setup>
import { LazyImg, Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
import { ref, reactive, watch } from "vue";
import loading from "@/assets/loading.png";
import error from "@/assets/error.png";
import { getTrendPageByUser } from "@/api/user";
import type { NoteSearch } from "@/type/note";
import Main from "@/pages/main/main.vue";

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
const noteList = ref<Array<any>>([]);
const noteTotal = ref(0);
const uid = history.state.uid;
const currentPage = ref(1);
const pageSize = 10;
const nid = ref("");
const mainShow = ref(false);

const close = () => {
  mainShow.value = false;
};

const toMain = (noteId: string) => {
  // console.log("11", nid);
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
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
  noteList.value = [] as Array<any>;
  getTrendPageByUser(currentPage.value, pageSize, uid).then((res) => {
    console.log("-----", res.data);
    setData(res);
  });
};

const initData = () => {
  console.log("----note", uid);
  console.log("----监听当前路由", window.location.href);
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
.feeds-container {
  position: relative;
  transition: width 0.5s;
  margin: 0 auto;

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
</style>
