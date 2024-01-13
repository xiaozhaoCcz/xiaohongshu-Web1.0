<template>
  <div class="feeds-container" v-infinite-scroll="loadMoreData">
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
      <template #item="{ item }">
        <div class="card">
          <el-image class="noteImg" @click="toMain(item.id)" :src="item.noteCover">
            <template #error>
              <div class="image-slot">
                <el-icon><icon-picture /></el-icon>
              </div>
            </template>
          </el-image>
          <!-- <LazyImg :url="url" @click="toMain(item.id)" class="fadeImg" /> -->
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
  </div>

  <Main
    v-show="mainShow"
    :nid="nid"
    class="animate__animated animate__zoomIn animate__delay-0.5s"
    @click-main="close"
  ></Main>
</template>
<script lang="ts" setup>
import { Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
import { ref, watch } from "vue";
import { getTrendPageByUser } from "@/api/user";
import type { NoteSearch } from "@/type/note";
import Main from "@/pages/main/main.vue";
import { options } from "@/constant/constant";

const props = defineProps({
  type: {
    type: Number,
    default: 1,
  },
});

watch(
  () => [props.type],
  ([newType], [oldType]) => {
    console.log("---newVal,oldVal", newType, oldType);
    currentPage.value = 1;
    noteList.value = [] as Array<any>;
    getNoteList(newType);
  }
);

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
  noteList.value.push(...records);
};

const getNoteList = (type: number) => {
  getTrendPageByUser(currentPage.value, pageSize, uid, type).then((res) => {
    console.log("-----", res.data);
    setData(res);
  });
};

const loadMoreData = () => {
  currentPage.value += 1;
  getNoteList(props.type);
};

const initData = () => {
  console.log("----note", uid);
  console.log("----监听当前路由", window.location.href);
  getNoteList(1);
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
</style>
