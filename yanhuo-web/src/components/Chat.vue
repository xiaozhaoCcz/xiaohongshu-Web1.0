<template>
  <div
    class="container"
    style="transition: background-color 0.4s ease 0s;
  hsla(0,0%,100%,0.98)"
  >
    <div class="chat-container">
      <header class="chat-header">
        <div class="header-left"></div>
        <div class="header-user">
          <el-avatar :src="acceptUser.avatar" />
          <span style="margin-left: 5px">{{ acceptUser.username }}</span>
        </div>
        <div class="header-tool">
          <More class="icon-item"></More>
        </div>
      </header>
      <hr color="#f4f4f4" />
      <main class="chat-main">
        <div class="chat-record" ref="ChatRef" @scroll="showScroll()">
          <div v-for="(item, index) in dataList" :key="index">
            <div class="message-my-item" v-if="item.acceptUid === acceptUser.id">
              <div class="message-my-conent">{{ item.content }}</div>
              <div class="user-avatar">
                <el-avatar :src="currentUser.avatar" />
              </div>
            </div>

            <div class="message-item" v-else>
              <div class="user-avatar">
                <el-avatar :src="acceptUser.avatar" />
              </div>
              <div class="message-conent">{{ item.content }}</div>
            </div>
          </div>
        </div>
        <hr color="#f4f4f4" />
        <div class="chat-input">
          <div class="input-tool">
            <div class="tool-left">
              <PieChart class="icon-item"></PieChart>
              <Picture class="icon-item"></Picture>
            </div>
            <div class="tool-history">
              <Clock class="icon-item"></Clock>
            </div>
          </div>

          <textarea type="textarea" v-model="content" class="input-content" rows="15" @keyup.enter="submit" />
        </div>
      </main>
    </div>
    <div class="close-cricle" @click="close">
      <div class="close close-mask-white">
        <Close style="width: 1.2em; height: 1.2em; color: rgba(51, 51, 51, 0.8)" />
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { More, PieChart, Picture, Clock, Close } from "@element-plus/icons-vue";
import { ref, onMounted, watch, nextTick } from "vue";
import { getUserById } from "@/api/user";
import { getAllChatRecord, sendMsg } from "@/api/im";
import { useUserStore } from "@/store/userStore";
import { useImStore } from "@/store/imStore";

const imStore = useImStore();
const userStore = useUserStore();
const props = defineProps({
  acceptUid: {
    type: String,
    default: "",
  },
});

const content = ref("");
const ChatRef = ref();
const currentUser = ref<any>({});
const acceptUser = ref<any>({});
const dataList = ref<any>();
const currentPage = ref(1);
const pageSize = 15;
const messageTotal = ref(0);

watch(
  () => imStore.message,
  (newVal) => {
    if (newVal.sendUid === acceptUser.value.id) {
      insertMessage(newVal);
    }
  },
  {
    deep: true,
  }
);

const insertMessage = async (message: any) => {
  dataList.value?.push(message);
  await nextTick();
  // 滚动到最底部
  ChatRef.value.lastElementChild.scrollIntoView({
    block: "start",
    behavior: "smooth",
  });
};

const emit = defineEmits(["clickChat"]);

const close = () => {
  emit("clickChat", props.acceptUid);
};

const submit = () => {
  if (content.value.trim().length <= 0) {
    return;
  }
  const message = ref({}) as any;
  message.sendUid = currentUser.value.id;
  message.acceptUid = acceptUser.value.id;
  message.content = content.value;
  message.msgType = 1;
  message.chatType = 0;
  sendMsg(message).then(() => {
    content.value = "";
    insertMessage(message);
  });
};

const showScroll = () => {
  const topval = ChatRef.value.scrollTop;
  if (topval === 0) {
    loadMoreData();
  }
};

const loadMoreData = () => {
  currentPage.value++;
  getAllChatRecordMethod();
};

const getAllChatRecordMethod = () => {
  getAllChatRecord(currentPage.value, pageSize, props.acceptUid).then((res) => {
    const { records, total } = res.data;
    messageTotal.value = total;
    records.forEach((item: any) => {
      dataList.value?.splice(0, 0, item);
    });
    if (dataList.value.length >= total) {
      ChatRef.value.scrollTop = 0;
    } else {
      ChatRef.value.scrollTop += ChatRef.value.clientHeight;
    }
  });
};

onMounted(async () => {
  currentUser.value = userStore.getUserInfo();
  getUserById(props.acceptUid).then((res) => {
    acceptUser.value = res.data;
  });
  dataList.value = [];
  getAllChatRecord(currentPage.value, pageSize, props.acceptUid).then(async (res) => {
    const { records, total } = res.data;
    messageTotal.value = total;
    records.forEach((item: any) => {
      dataList.value.splice(0, 0, item);
    });
    await nextTick();
    // 滚动到最底部
    ChatRef.value.lastElementChild.scrollIntoView({
      block: "start",
      behavior: "smooth",
    });
  });
});
</script>
<style lang="less" scoped>
.icon-item {
  width: 1.2em;
  height: 1.2em;
  margin-right: 5px;
  color: rgba(51, 51, 51, 0.8);
}

.container {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  z-index: 20;
  overflow: auto;

  .chat-container {
    width: 65%;
    margin: 0 auto;
    height: 90%;
    min-width: 800px;
    transition:
      transform 0.4s ease 0s,
      width 0.4s ease 0s;
    transform: translate(104px, 32px) scale(1);
    overflow: visible;
    box-shadow:
      0 8px 64px 0 rgba(0, 0, 0, 0.04),
      0 1px 4px 0 rgba(0, 0, 0, 0.02);
    border-radius: 20px;
    background-color: #fff;
    transform-origin: left top;
    z-index: 100;

    .chat-header {
      height: 60px;
      width: 100%;
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-user {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }

    .chat-main {
      height: 100%;

      .chat-record {
        height: 60%;
        padding: 0 20px;
        overflow-y: scroll;

        .message-item {
          display: flex;
          justify-content: left;
          align-items: center;
          margin: 20px 0;

          .message-conent {
            margin-left: 5px;
            padding: 4px 10px;
            border: 1px solid #f4f4f4;
            background-color: #fff;
            border-radius: 8px;
            font-size: 16px;
          }
        }

        .message-my-item {
          display: flex;
          justify-content: right;
          align-items: center;
          margin: 20px 0;

          .message-my-conent {
            margin-right: 5px;
            padding: 4px 10px;
            color: #fff;
            background-color: rgb(0, 170, 255);
            border-radius: 8px;
            font-size: 16px;
          }
        }
      }

      .chat-input {
        height: 40%;

        .input-tool {
          display: flex;
          justify-content: space-between;
          height: 30px;
          padding: 0 5px;
        }

        .input-content {
          width: 100%;
          padding: 0 10px;
          resize: none;
          border: 0px;
          outline: none;
        }
      }
    }
  }

  .close-cricle {
    left: 18vw;
    top: 1.3vw;
    position: fixed;
    display: flex;
    z-index: 100;
    cursor: pointer;

    .close-mask-white {
      box-shadow:
        0 2px 8px 0 rgba(0, 0, 0, 0.04),
        0 1px 2px 0 rgba(0, 0, 0, 0.02);
      border: 1px solid rgba(0, 0, 0, 0.08);
    }

    .close {
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 100%;
      width: 40px;
      height: 40px;
      border-radius: 40px;
      cursor: pointer;
      transition: all 0.3s;
      background-color: #fff;
    }
  }
}
</style>
