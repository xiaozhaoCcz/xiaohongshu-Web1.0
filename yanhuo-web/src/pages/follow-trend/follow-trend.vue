<template>
  <div class="container" @scroll="handleScroll">
    <div class="feeds-loading-top" v-show="topLoading">
      <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
    </div>
    <ul class="trend-container">
      <li class="trend-item" v-for="(item, index) in trendData" :key="index">
        <a class="user-avatar">
          <img class="avatar-item" :src="item.avatar" />
        </a>
        <div class="main">
          <div class="info">
            <div class="user-info">
              <a class>{{ item.username }}</a>
            </div>
            <div class="interaction-hint">
              <span>{{ item.time }}</span>
            </div>
            <div class="interaction-content" @click="toMain(item.nid)">
              {{ item.content }}
            </div>
            <div class="interaction-imgs" @click="toMain(item.nid)">
              <div class="details-box" v-for="(url, index) in item.imgUrls" :key="index">
                <img :src="url" class="animate__animated animate__fadeIn" />
              </div>
            </div>
            <div class="interaction-footer">
              <div class="icon-item">
                <i
                  class="iconfont icon-follow-fill"
                  style="width: 1em; height: 1em"
                  @click="like(item.nid, item.uid, index, -1)"
                  v-if="item.isLike"
                ></i>
                <i
                  class="iconfont icon-follow"
                  style="width: 1em; height: 1em"
                  @click="like(item.nid, item.uid, index, 1)"
                  v-else
                ></i
                ><span class="count">{{ item.likeCount }}</span>
              </div>
              <div class="icon-item">
                <ChatRound style="width: 0.9em; height: 0.9em" /><span class="count">{{
                  item.commentCount
                }}</span>
              </div>
              <div class="icon-item"><More style="width: 1em; height: 1em" /></div>
            </div>
          </div>
        </div>
      </li>
    </ul>
    <div class="feeds-loading">
      <RefreshRight style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
    </div>
    <FloatingBtn @click-refresh="refresh"></FloatingBtn>
    <Main v-show="mainShow" :nid="nid" class="mainShow" @click-main="close"></Main>
  </div>
</template>
<script lang="ts" setup>
import { ChatRound, More, RefreshRight } from "@element-plus/icons-vue";
import { ref, onMounted } from "vue";
import { getFollowTrendPage } from "@/api/follower";
import { formateTime } from "@/utils/util";
import FloatingBtn from "@/components/FloatingBtn.vue";
import Main from "@/pages/main/main.vue";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";
import { likeOrCollectionByDTO } from "@/api/likeOrCollection";

const currentPage = ref(1);
const pageSize = ref(5);
const trendData = ref<Array<any>>([]);
const trendTotal = ref(0);
const topLoading = ref(false);
const mainShow = ref(false);
const nid = ref("");
const likeOrCollectionDTO = ref<LikeOrCollectionDTO>({});

const getFollowTrends = () => {
  getFollowTrendPage(currentPage.value, pageSize.value).then((res) => {
    console.log("--trends", res.data);
    const { records, total } = res.data;
    records.forEach((item: any) => {
      const date = formateTime(item.time);
      item.time = date;
      trendData.value.push(item);
    });
    trendTotal.value = total;
  });
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
  if (clientHeight + scrollTop >= scrollHeight && currentPage.value <= trendTotal.value) {
    //快到底时----加载
    console.log("到达底部");
    loadMoreData();
  }
};

const loadMoreData = () => {
  currentPage.value += 1;
  getFollowTrends();
};

const toMain = (noteId: string) => {
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
          trendData.value = [];
          getFollowTrends();
          topLoading.value = false;
        }, 1000);
      }
    }, 10); //定时调用函数使其更顺滑
  } else {
    document.documentElement.scrollTop = 0;
    topLoading.value = true;
    setTimeout(() => {
      currentPage.value = 1;
      trendData.value = [];
      getFollowTrends();
      topLoading.value = false;
    }, 1000);
  }
};

const like = (nid: string, uid: string, index: number, val: number) => {
  likeOrCollectionDTO.value.likeOrCollectionId = nid;
  likeOrCollectionDTO.value.publishUid = uid;
  likeOrCollectionDTO.value.type = 1;
  likeOrCollectionByDTO(likeOrCollectionDTO.value).then(() => {
    trendData.value[index].isLike = val == 1;
    trendData.value[index].likeCount += val;
  });
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

const initData = () => {
  getFollowTrends();
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
.container {
  flex: 1;
  padding: 0 24px;
  padding-top: 72px;
  width: 67%;
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

  .trend-container {
    .trend-item {
      display: flex;
      flex-direction: row;
      padding-top: 24px;
      max-width: 100vw;

      .user-avatar {
        margin-right: 24px;
        flex-shrink: 0;

        .avatar-item {
          width: 48px;
          height: 48px;
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          border-radius: 100%;
          border: 1px solid rgba(0, 0, 0, 0.08);
          object-fit: cover;
        }
      }

      .main {
        flex-grow: 1;
        flex-shrink: 1;
        display: flex;
        flex-direction: row;
        padding-bottom: 12px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);

        .info {
          flex-grow: 1;
          flex-shrink: 1;

          .user-info {
            display: flex;
            flex-direction: row;
            align-items: center;
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 4px;

            a {
              color: #333;
            }
          }
          .interaction-hint {
            font-size: 14px;
            color: rgba(51, 51, 51, 0.6);
            margin-bottom: 8px;
          }
          .interaction-content {
            display: flex;
            font-size: 14px;
            color: #333;
            margin-bottom: 12px;
            line-height: 140%;
            cursor: pointer;
          }

          .interaction-imgs {
            display: flex;
            .details-box {
              width: 25%;
              border-radius: 4px;
              margin: 8px 12px 0 0;
              cursor: pointer;

              img {
                width: 100%;
                height: 230px;
                display: flex;
                border-radius: 4px;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                border: 1px solid rgba(0, 0, 0, 0.08);
                object-fit: cover;
              }
            }
          }

          .interaction-footer {
            margin: 8px 12px 0 0;
            padding: 0 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .icon-item {
              display: flex;
              justify-content: left;
              align-items: center;
              color: rgba(51, 51, 51, 0.929);
              .count {
                margin-left: 3px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
