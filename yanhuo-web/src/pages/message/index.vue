<template>
  <div class="container">
    <div v-if="isLogin">
      <div class style="height: 72px">
        <div class="reds-sticky">
          <div class="reds-tabs-list">
            <el-badge :value="_countMessage.chatCount" :max="99" :hidden="_countMessage.chatCount == 0">
              <div :class="type === 3 ? 'reds-tab-item active tab-item' : 'reds-tab-item tab-item'">
                <div class="badge-container" @click="toPage(3)">
                  <span>我的消息</span>
                </div>
              </div>
            </el-badge>
            <el-badge :value="_countMessage.commentCount" :max="99" :hidden="_countMessage.commentCount == 0">
              <div :class="type === 1 ? 'reds-tab-item active tab-item' : 'reds-tab-item tab-item'">
                <div class="badge-container" @click="toPage(1)">
                  <span>评论和@</span>
                </div>
              </div>
            </el-badge>
            <el-badge
              :value="_countMessage.likeOrCollectionCount"
              :max="99"
              :hidden="_countMessage.likeOrCollectionCount == 0"
            >
              <div :class="type === 0 ? 'reds-tab-item active tab-item' : 'reds-tab-item tab-item'">
                <div class="badge-container" @click="toPage(0)">
                  <span>赞和收藏</span>
                </div>
              </div>
            </el-badge>
            <el-badge :value="_countMessage.followCount" :max="99" :hidden="_countMessage.followCount == 0">
              <div :class="type === 2 ? 'reds-tab-item active tab-item' : 'reds-tab-item tab-item'">
                <div class="badge-container" @click="toPage(2)">
                  <span>新增关注</span>
                </div>
              </div>
            </el-badge>
          </div>
          <div class="divider" style="margin: 16px 32px 0px"></div>
        </div>
      </div>
      <Message v-if="type == 3"></Message>
      <Comment v-if="type == 1" @click-main="toMain"></Comment>
      <LikeCollection v-if="type == 0" @click-main="toMain"></LikeCollection>
      <Follower v-if="type == 2"></Follower>
      <!-- <router-view /> -->

      <Main
        v-show="mainShow"
        :nid="nid"
        :nowTime="new Date()"
        class="animate__animated animate__zoomIn animate__delay-0.5s"
        @click-main="close"
      ></Main>

      <el-backtop :bottom="80" :right="24">
        <div class="back-top">
          <Top style="width: 1.2em; height: 1.2em" color="rgba(51, 51, 51, 0.8)" />
        </div>
      </el-backtop>
    </div>
    <div v-else>
      <el-empty description="用户未登录" />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Top } from "@element-plus/icons-vue";
import { ref, watchEffect } from "vue";
import Message from "@/pages/message/children/message.vue";
import LikeCollection from "@/pages/message/children/like-collection.vue";
import Follower from "@/pages/message/children/follower.vue";
import Comment from "@/pages/message/children/comment.vue";
import Main from "@/pages/main/main.vue";
import { useImStore } from "@/store/imStore";
import { clearMessageCount } from "@/api/im";
import { useUserStore } from "@/store/userStore";
const imStore = useImStore();
const userStore = useUserStore();

const type = ref(3);
const nid = ref("");
const currentUid = ref("");
const mainShow = ref(false);
const _countMessage = ref({
  chatCount: 0,
  likeOrCollectionCount: 0,
  commentCount: 0,
  followCount: 0,
});
const isLogin = ref(false);

watchEffect(() => {
  _countMessage.value = imStore.countMessage;
});

const toPage = (val: number) => {
  const _countMessage = imStore.countMessage;
  clearMessageCount(currentUid.value, val).then(() => {
    switch (val) {
      case 0:
        _countMessage.likeOrCollectionCount = 0;
        break;
      case 1:
        _countMessage.commentCount = 0;
        break;
      default:
        _countMessage.followCount = 0;
        break;
    }
    imStore.setCountMessage(_countMessage);
    type.value = val;
  });
};

const close = () => {
  mainShow.value = false;
};

const toMain = (val: string) => {
  nid.value = val;
  mainShow.value = true;
};

const initData = () => {
  isLogin.value = userStore.isLogin();
  if (isLogin.value) {
    currentUid.value = userStore.getUserInfo().id;
  }
};
initData();
</script>
<style lang="less" scoped>
.container {
  flex: 1;
  padding: 0 24px;
  padding-top: 72px;
  width: 67%;
  margin: 0 auto;

  .reds-sticky {
    top: 72px;
    position: fixed;
    z-index: 1;
    width: 40rem;
    box-sizing: border-box;
    height: 72px;
    padding-top: 16px;
    justify-content: center;
    flex-direction: column;
    background: #fff;

    .reds-tabs-list {
      justify-content: flex-start;
      display: flex;
      flex-wrap: nowrap;
      position: relative;
      font-size: 16px;
      padding: 0 32px;

      .active {
        font-weight: 600;
        color: #333;
        background-color: rgba(0, 0, 0, 0.03);
        border-radius: 20px;
      }

      .reds-tab-item {
        padding: 0px 16px;
        margin-right: 0px;
        font-size: 16px;
        display: flex;
        align-items: center;
        box-sizing: border-box;
        height: 40px;
        cursor: pointer;
        color: rgba(51, 51, 51, 0.8);
        white-space: nowrap;
        transition: transform 0.3s cubic-bezier(0.2, 0, 0.25, 1);
        z-index: 1;

        .badge-container {
          position: relative;
        }
      }
    }

    .divider {
      margin: 4px 8px;
      list-style: none;
      height: 0;
      border: solid rgba(0, 0, 0, 0.08);
      border-width: 1px 0 0;
    }

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
  }
}
</style>
