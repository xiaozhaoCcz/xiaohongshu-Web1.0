<template>
  <div class="feeds-container" v-infinite-scroll="loadMoreData" :infinite-scroll-distance="50">
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
              <div class="top-tag-area" v-show="type === 1 && item.pinned === 1">
                <div class="top-wrapper">置顶</div>
              </div>
            </div>
          </template>
        </el-skeleton>
      </template>
    </Waterfall>
  </div>

  <Main
    v-show="mainShow"
    :nid="nid"
    :nowTime="new Date()"
    class="animate__animated animate__zoomIn animate__delay-0.5s"
    @click-main="close"
  ></Main>
</template>

<script lang="ts" setup>
import { Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
import { ref, watch } from "vue";
import { getTrendPageByUser } from "@/api/user";
import Main from "@/pages/main/main.vue";
import { options } from "@/constant/constant";
import { useRoute } from "vue-router";
const route = useRoute();

const props = defineProps({
  type: {
    type: Number,
    default: 1,
  },
});

watch(
  () => [props.type],
  ([newType]) => {
    currentPage.value = 1;
    noteList.value = [] as Array<any>;
    getNoteList(newType);
  }
);

const noteList = ref<Array<any>>([]);
const noteTotal = ref(0);
const uid = route.query.uid as string;
const currentPage = ref(1);
const pageSize = 10;
const nid = ref("");
const mainShow = ref(false);

const handleLoad = (item: any) => {
  item.isLoading = true;
};

const close = () => {
  mainShow.value = false;
};

const toMain = (noteId: string) => {
  // router.push({ name: "main", state: { nid: nid } });
  nid.value = noteId;
  mainShow.value = true;
};

const setData = (res: any) => {
  const { records, total } = res.data;
  console.log(records, total);
  noteTotal.value = total;
  noteList.value.push(...records);
};

const getNoteList = (type: number) => {
  getTrendPageByUser(currentPage.value, pageSize, uid, type).then((res) => {
    setData(res);
  });
};

const loadMoreData = () => {
  currentPage.value += 1;
  getNoteList(props.type);
};

const initData = () => {
  getNoteList(1);
};

initData();
</script>

<style lang="less" scoped>
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

  .card {
    position: relative;

    .top-tag-area {
      position: absolute;
      left: 12px;
      top: 12px;
      z-index: 4;

      .top-wrapper {
        background: #ff2442;
        border-radius: 999px;
        font-weight: 500;
        color: #fff;
        line-height: 120%;
        font-size: 12px;
        padding: 5px 8px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
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
</style>
