<template>
  <div class="container" v-infinite-scroll="loadMoreData" :infinite-scroll-distance="50">
    <div v-if="isLogin">
      <ul class="trend-container">
        <li class="trend-item" v-for="(item, index) in trendData" :key="index">
          <a class="user-avatar">
            <img class="avatar-item" :src="item.avatar" @click="toUser(item.uid)" />
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
                  <el-image
                    v-if="!item.isLoading"
                    :src="url"
                    @load="handleLoad(item)"
                    style="height: 230px; width: 100%"
                  >
                  </el-image>
                  <el-image
                    v-else
                    :src="url"
                    class="note-img animate__animated animate__fadeIn animate__delay-0.5s"
                    fit="cover"
                  ></el-image>
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
                  <ChatRound style="width: 0.9em; height: 0.9em" /><span class="count">{{ item.commentCount }}</span>
                </div>
                <div class="icon-item">
                  <More style="width: 1em; height: 1em" />
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
      <div class="feeds-loading">
        <Refresh style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
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
    <div v-else>
      <el-empty description="用户未登录" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ChatRound, More, Refresh } from "@element-plus/icons-vue";
import { ref } from "vue";
import { getFollowTrendPage } from "@/api/follower";
import { formateTime, refreshTab } from "@/utils/util";
import FloatingBtn from "@/components/FloatingBtn.vue";
import Main from "@/pages/main/main.vue";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";
import { likeOrCollectionByDTO } from "@/api/likeOrCollection";
import { useRouter } from "vue-router";
import { useUserStore } from "@/store/userStore";

const router = useRouter();
const userStore = useUserStore();
const currentPage = ref(1);
const pageSize = ref(5);
const trendData = ref<Array<any>>([]);
const trendTotal = ref(0);
const topLoading = ref(false);
const mainShow = ref(false);
const nid = ref("");
const likeOrCollectionDTO = ref<LikeOrCollectionDTO>({
  likeOrCollectionId: "",
  publishUid: "",
  type: 0,
});
const isLogin = ref(false);

const handleLoad = (item: any) => {
  item.isLoading = true;
};

const toUser = (uid: string) => {
  //router.push({ name: "user", state: { uid: uid } });
  router.push({ name: "user", query: { uid: uid } });
};

const getFollowTrends = () => {
  getFollowTrendPage(currentPage.value, pageSize.value).then((res) => {
    const { records, total } = res.data;
    console.log(records, total);
    records.forEach((item: any) => {
      item.time = formateTime(item.time);
      trendData.value.push(item);
    });
    trendTotal.value = total;
  });
};

const loadMoreData = () => {
  currentPage.value += 1;
  getFollowTrends();
};

const toMain = (noteId: string) => {
  nid.value = noteId;
  mainShow.value = true;
};

const close = (nid: string, val: any) => {
  const index = trendData.value.findIndex((item) => item.nid === nid);
  console.log("---val", val, index);
  const _data = trendData.value[index];
  if (_data.isLike != val.isLike) {
    _data.isLike = val.isLike;
    _data.likeCount += val.isLike ? 1 : -1;
  }
  if (val.isComment) {
    _data.commentCount += 1;
  }
  mainShow.value = false;
};

const refresh = () => {
  refreshTab(() => {
    topLoading.value = true;
    setTimeout(() => {
      currentPage.value = 1;
      trendData.value = [];
      getFollowTrends();
      topLoading.value = false;
    }, 500);
  });
};

const like = (nid: string, uid: string, index: number, val: number) => {
  likeOrCollectionDTO.value.likeOrCollectionId = nid;
  likeOrCollectionDTO.value.publishUid = uid;
  likeOrCollectionDTO.value.type = 1;
  likeOrCollectionByDTO(likeOrCollectionDTO.value).then(() => {
    if (val < 0 && trendData.value[index].likeCount == 0) {
      return;
    }
    trendData.value[index].isLike = val == 1;
    trendData.value[index].likeCount += val;
  });
};

const initData = () => {
  isLogin.value = userStore.isLogin();
  getFollowTrends();
};

initData();
</script>

<style lang="less" scoped>
.container {
  flex: 1;
  padding: 0 24px;
  padding-top: 72px;
  width: 67%;
  height: 100vh;
  margin: 0 auto;

  .feeds-loading {
    margin: 3vh;
    text-align: center;
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

              .note-img {
                width: 100%;
                height: 230px;
                display: flex;
                border-radius: 4px;
                align-items: center;
                justify-content: center;
                cursor: pointer;
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
