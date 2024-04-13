<template>
  <div>
    <ul class="message-container">
      <li class="message-item" v-for="(item, index) in dataList" :key="index">
        <a class="user-avatar">
          <!-- https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png -->
          <img class="avatar-item" :src="item.avatar" @click="toUser(item.uid)" />
        </a>
        <div class="main">
          <div class="info">
            <div class="user-info">
              <a class>{{ item.username }}</a>
              <div class="interaction-hint">
                <span>{{ item.time }}</span>
              </div>
            </div>

            <div class="interaction-content" @click="toChat(item.uid, index)">
              <span>{{ item.content }}</span>
              <div class="msg-count" v-show="item.count > 0">{{ item.count }}</div>
            </div>
          </div>
        </div>
      </li>
    </ul>
    <Chat
      v-if="chatShow"
      :acceptUid="acceptUid"
      class="animate__animated animate__zoomIn animate__delay-0.5s"
      @click-chat="close"
    ></Chat>
  </div>
</template>

<script lang="ts" setup>
import { useImStore } from "@/store/imStore";
import { ref, watchEffect } from "vue";
import { formateTime } from "@/utils/util";
import Chat from "@/components/Chat.vue";
import { clearMessageCount } from "@/api/im";
import { useRouter } from "vue-router";

const router = useRouter();
const imStore = useImStore();
const dataList = ref<Array<any>>([]);
const chatShow = ref(false);
const acceptUid = ref("");

const toUser = (uid: string) => {
  router.push({ name: "user", query: { uid: uid } });
};

watchEffect(() => {
  dataList.value = [];
  const _countMessage = imStore.countMessage;
  _countMessage.chatCount = 0;
  imStore.userList.forEach((item) => {
    item.time = formateTime(item.timestamp);
    _countMessage.chatCount += item.count;
    dataList.value.push(item);
  });
  imStore.setCountMessage(_countMessage);
});

const toChat = (uid: string, index: number) => {
  const _countMessage = imStore.countMessage;
  clearMessageCount(uid, 3).then(() => {
    const chatCount = dataList.value[index].count;
    _countMessage.chatCount -= chatCount;
    dataList.value[index].count = 0;
    imStore.setCountMessage(_countMessage);
    acceptUid.value = uid;
    chatShow.value = true;
  });
};

const close = (uid: string) => {
  const index = dataList.value.findIndex((item) => item.uid === uid);
  const _countMessage = imStore.countMessage;
  clearMessageCount(uid, 3).then(() => {
    const chatCount = dataList.value[index].count;
    _countMessage.chatCount -= chatCount;
    dataList.value[index].count = 0;
    imStore.setCountMessage(_countMessage);
    chatShow.value = false;
  });
};
</script>

<style lang="less" scoped>
.message-container {
  width: 40rem;

  .message-item {
    display: flex;
    flex-direction: row;
    padding-top: 24px;

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
          justify-content: space-between;
          margin-bottom: 8px;

          a {
            color: #333;
            font-size: 16px;
            font-weight: 600;
          }

          .interaction-hint {
            font-size: 12px;
            color: rgba(51, 51, 51, 0.6);
          }
        }

        .interaction-content {
          display: flex;
          font-size: 14px;
          color: #333;
          line-height: 140%;
          cursor: pointer;
          justify-content: space-between;
          align-items: center;

          .msg-count {
            width: 20px;
            height: 20px;
            line-height: 20px;
            font-size: 13px;
            color: #fff;
            background-color: red;
            text-align: center;
            border-radius: 100%;
          }
        }
      }
    }
  }
}
</style>
