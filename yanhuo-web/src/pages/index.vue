<template>
  <div class="container" id="container">
    <div class="top">
      <header class="mask-paper">
        <a style="display: flex">烟火</a>
        <div class="tool-box"></div>
        <div class="input-box" id="sujContainer">
          <input
            type="text"
            v-model="keyword"
            class="search-input"
            placeholder="搜索小红书"
            @input="changeInput"
            @focus="focusInput"
            @keyup.enter="searchPage"
            ref="SearchInput"
          />
          <div class="input-button">
            <div class="close-icon" v-show="showClose" @click="clearInput">
              <Close
                style="width: 1.2em; height: 1.2em; margin-right: 20px; margin-top: 5px"
              />
            </div>
            <div class="search-icon" @click="searchPage">
              <Search
                style="width: 1.2em; height: 1.2em; margin-right: 20px; margin-top: 5px"
              />
            </div>
          </div>
          <SearchContainer v-show="showSearch" :recordList="recordList"></SearchContainer>
          <SujContainer v-show="showHistory"></SujContainer>
        </div>
        <div class="right"></div>
      </header>
    </div>
    <div class="main">
      <div class="side-bar">
        <ul class="channel-list">
          <li :class="activeLink == 0 ? 'active-channel' : ''" @click="toLink(0)">
            <a class="link-wrapper">
              <House style="width: 1em; height: 1em; margin-right: 8px" /><span
                class="channel"
                >发现</span
              ></a
            >
          </li>
          <li :class="activeLink == 1 ? 'active-channel' : ''" @click="toLink(1)">
            <Star style="width: 1em; height: 1em; margin-right: 8px" /><span
              class="channel"
            >
              动态</span
            >
          </li>
          <li :class="activeLink == 2 ? 'active-channel' : ''" @click="toLink(2)">
            <Bell style="width: 1em; height: 1em; margin-right: 8px" /><span
              class="channel"
            >
              消息</span
            >
          </li>
          <li :class="activeLink == 3 ? 'active-channel' : ''" @click="toLink(3)">
            <CirclePlus style="width: 1em; height: 1em; margin-right: 8px" /><span
              class="channel"
            >
              发布</span
            >
          </li>
          <li v-if="userInfo == null">
            <el-button type="danger" round>登录</el-button>
          </li>
          <li v-else :class="activeLink == 4 ? 'active-channel' : ''" @click="toLink(4)">
            <el-avatar :src="userInfo.avatar" :size="22" />
            <span class="channel">我</span>
          </li>
        </ul>

        <div class="information-container">
          <div class="information-pad" v-show="padShow">
            <div class="container">
              <div>
                <div>
                  <div class="group-wrapper">
                    <div class="menu-item hover-effect">
                      <span>关于小红书</span>
                      <div class="icon">
                        <ArrowRight style="width: 1em; height: 1em; margin-right: 8px" />
                      </div>
                    </div>
                    <div class="menu-item hover-effect">
                      <span>隐私，协议</span>
                      <div class="icon">
                        <ArrowRight style="width: 1em; height: 1em; margin-right: 8px" />
                      </div>
                    </div>
                    <div class="menu-item hover-effect">
                      <span>帮助与客服</span>
                    </div>
                  </div>
                  <div class="divider"></div>
                </div>
                <div>
                  <div class="group-wrapper">
                    <div class="group-header">访问方式</div>
                    <div class="menu-item hover-effect">
                      <span>键盘快捷键</span>
                      <div class="icon">
                        <Search style="width: 1em; height: 1em; margin-right: 8px" />
                      </div>
                    </div>
                    <div class="menu-item hover-effect">
                      <span>添加小红书到桌面</span>
                      <div class="icon">
                        <ArrowRight style="width: 1em; height: 1em; margin-right: 8px" />
                      </div>
                    </div>
                    <div class="menu-item hover-effect">
                      <span>小窗模式</span>
                    </div>
                  </div>
                  <div class="divider"></div>
                </div>
                <div>
                  <div class="group-wrapper">
                    <div class="group-header">设置</div>
                    <div class="menu-item hover-effect">
                      <span>深色模式</span>
                      <div class="multistage-toggle component">
                        <button class="toggle-item active">
                          <div class="icon-wrapper">
                            <Sunny style="width: 1em; height: 1em" />
                          </div>
                        </button>
                        <button class="toggle-item">
                          <div class="icon-wrapper">
                            <Moon style="width: 1em; height: 1em" />
                          </div>
                        </button>
                      </div>
                    </div>
                    <div class="menu-item hover-effect">
                      <span>推出登录</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="information-wrapper" @click="loadPad">
            <More style="width: 1em; height: 1em; margin-right: 8px" />
            <span class="channel"> 更多</span>
          </div>
        </div>
      </div>
      <div class="main-content with-side-bar">
        <router-view />
      </div>
    </div>

    <Login v-show="loginShow" @click-child="close"></Login>
  </div>
</template>

<script lang="ts" setup>
import {
  Search,
  Sunny,
  Moon,
  Close,
  House,
  Star,
  Bell,
  ArrowRight,
  More,
  CirclePlus,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import Login from "@/pages/login.vue";
import { ref, watch, onMounted } from "vue";
import { useUserStore } from "@/store/userStore";
import { useSearchStore } from "@/store/searchStore";
import SujContainer from "@/components/SujContainer.vue";
import SearchContainer from "@/components/SearchContainer.vue";
import { getRecordByKeyWord } from "@/api/search";
import { getRandomString } from "@/utils/util";

const router = useRouter();
const userStore = useUserStore();
const searchStore = useSearchStore();
const loginShow = ref(true);
const userInfo = ref<any>({});
const showHistory = ref(false);
const showSearch = ref(false);
const keyword = ref("");
const showClose = ref(false);
const SearchInput = ref();
const recordList = ref<Array<string>>([]);
const activeLink = ref(1);
const padShow = ref(false);

const routerList = ["/dashboard", "/followTrend", "/notice", "/push", "/user"];

// 监听外部点击
onMounted(() => {
  document.getElementById("container")!.addEventListener("click", function (e) {
    let event = e || window.event;
    let target = event.target || (event.srcElement as any);
    // if(target.id == "name") {
    if (document.getElementById("sujContainer")!.contains(target)) {
      console.log("在div内");
    } else {
      showHistory.value = false;
      showSearch.value = false;
    }
  });
});

const searchPage = () => {
  console.log("search", keyword.value);
  // 1.storage中添加搜索记录
  searchStore.setKeyword(keyword.value);
  if (keyword.value.length > 0) {
    searchStore.pushRecord(keyword.value);
    searchStore.setSeed(getRandomString(12));
  }
  showSearch.value = false;
};

watch(
  () => [searchStore.seed],
  (newVal, oldVal) => {
    console.log("seed", newVal, oldVal);
    keyword.value = searchStore.keyWord;
    showHistory.value = false;
  }
);

const changeInput = (e: any) => {
  const { value } = e.target;
  keyword.value = value;
  showClose.value = keyword.value == "" ? false : true;
  showSearch.value = keyword.value.length == 0 ? false : true;
  showHistory.value = keyword.value.length > 0 ? false : true;
  if (keyword.value.length > 0) {
    getRecordByKeyWord(keyword.value).then((res) => {
      recordList.value = res.data;
    });
  }
};

const focusInput = () => {
  showSearch.value = keyword.value.length == 0 ? false : true;
  showHistory.value = keyword.value.length > 0 ? false : true;
};

const clearInput = () => {
  keyword.value = "";
  showClose.value = false;
  showHistory.value = true;
  showSearch.value = false;
  SearchInput.value.focus();
};

const toLink = (num: number) => {
  activeLink.value = num;
  const url = routerList[num];
  if (url === "/user") {
    router.push({ name: "user", state: { uid: userInfo.value.id } });
    return;
  }
  router.push({ path: url });
};

const close = (val: boolean) => {
  loginShow.value = val;
  userInfo.value = userStore.getUserInfo();
};

const loadPad = () => {
  padShow.value = !padShow.value;
};

const connectWs = (uid: string) => {
  const ws = new WebSocket(`ws://127.0.0.1:8802/im/ws/${uid}`);
  ws.onopen = () => {
    console.log("连接成功");
  };
  ws.onclose = () => {
    console.log("连接断开");
  };
  ws.onmessage = (e) => {
    console.log("收到消息", e);
  };
};

const initData = () => {
  userInfo.value = userStore.getUserInfo();
  const url = window.location.href;
  const path = url.substring(url.lastIndexOf("/"), url.length);
  activeLink.value = routerList.findIndex((item) => item === path);
  if (userInfo.value != null) {
    loginShow.value = false;
    connectWs(userInfo.value.id);
  }
};
initData();
</script>

<style lang="less" scoped>
.container {
  max-width: 1728px;
  background-color: #fff;
  margin: 0 auto;

  .top {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 100vw;
    height: 72px;
    position: fixed;
    left: 0;
    top: 0;
    z-index: 10;
    align-items: center;
    background: #fff;

    header {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      max-width: 1728px;
      height: 72px;
      padding: 0 16px 0 24px;
      z-index: 10;

      .tool-box {
        width: 24px;
        height: 70px;
        position: absolute;
        left: 0;
        top: 0;
      }

      .input-box {
        height: 40px;
        position: fixed;
        left: 50%;
        transform: translate(-50%);

        @media screen and (max-width: 695px) {
          display: none;
        }

        @media screen and (min-width: 960px) and (max-width: 1191px) {
          width: calc(-36px + 50vw);
        }

        @media screen and (min-width: 1192px) and (max-width: 1423px) {
          width: calc(-33.6px + 40vw);
        }

        @media screen and (min-width: 1424px) and (max-width: 1727px) {
          width: calc(-42.66667px + 33.33333vw);
        }

        @media screen and (min-width: 1728px) {
          width: 533.33333px;
        }
        .search-input {
          padding: 0 84px 0 16px;
          width: 100%;
          height: 100%;
          font-size: 16px;
          line-height: 120%;
          color: #333;
          caret-color: #ff2442;
          background: rgba(0, 0, 0, 0.03);
          border-radius: 999px;
        }

        .input-button {
          position: absolute;
          right: 0;
          top: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
          color: rgba(51, 51, 51, 0.8);

          .close-icon .search-icon {
            width: 40px;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            color: rgba(51, 51, 51, 0.8);
          }
        }
      }
    }
  }

  .main {
    display: flex;

    .side-bar {
      @media screen and (max-width: 695px) {
        display: none;
      }
      @media screen and (min-width: 696px) and (max-width: 959px) {
        display: none;
      }

      @media screen and (min-width: 960px) and (max-width: 1191px) {
        width: calc(-18px + 25vw);
        margin-left: 12px;
      }

      @media screen and (min-width: 1192px) and (max-width: 1423px) {
        width: calc(-16.8px + 20vw);
        margin-left: 12px;
      }

      @media screen and (min-width: 1424px) and (max-width: 1727px) {
        width: calc(-21.33333px + 16.66667vw);
        margin-left: 16px;
      }

      @media screen and (min-width: 1728px) {
        width: 266.66667px;
        margin-left: 16px;
      }

      height: calc(100vh - 72px);
      overflow-y: scroll;
      background-color: #fff;
      display: flex;
      flex-direction: column;
      flex-shrink: 0;
      padding-top: 16px;
      margin-top: 72px;
      position: fixed;
      overflow: visible;

      .channel-list {
        min-height: auto;
        -webkit-user-select: none;
        user-select: none;

        .active-channel {
          background-color: rgba(0, 0, 0, 0.03);
          border-radius: 999px;
          color: #333;
        }

        li {
          padding-left: 16px;
          min-height: 48px;
          display: flex;
          align-items: center;
          cursor: pointer;
          margin-bottom: 8px;
          color: rgba(51, 51, 51, 0.6);

          .link-wrapper {
            display: flex;
            width: 100%;
            height: 48px;
            align-items: center;
          }
        }

        .channel {
          font-size: 16px;
          font-weight: 600;
          margin-left: 12px;
          color: #333;
        }
      }

      .information-container {
        display: inline-block;
        width: 100%;
        color: #333;
        font-size: 16px;
        position: absolute;
        bottom: 0;

        .information-pad {
          z-index: 16;
          margin-bottom: 4px;
          width: 100%;

          .container {
            width: 100%;
            background: #fff;
            box-shadow: 0 4px 32px 0 rgba(0, 0, 0, 0.08), 0 1px 4px 0 rgba(0, 0, 0, 0.04);
            border-radius: 12px;

            .divider {
              margin: 0px 12px;
              list-style: none;
              height: 0;
              border: 1px solid rgba(0, 0, 0, 0.08);
              border-width: 1px 0 0;
            }

            .group-wrapper {
              padding: 4px;

              .group-header {
                display: flex;
                align-items: center;
                padding: 0 12px;
                font-weight: 400;
                height: 32px;
                color: rgba(51, 51, 51, 0.6);
                font-size: 12px;
              }

              .menu-item {
                height: 40px;
                color: rgba(51, 51, 51, 0.8);
                font-size: 16px;
                border-radius: 8px;
                display: flex;
                align-items: center;
                padding: 0 12px;
                font-weight: 400;

                .icon {
                  color: rgba(51, 51, 51, 0.3);
                  margin-left: auto;
                }

                .component {
                  margin-left: auto;
                }

                .multistage-toggle {
                  position: relative;
                  background: rgba(0, 0, 0, 0.03);
                  display: flex();
                  padding: 2px;
                  border-radius: 999px;
                  cursor: pointer;

                  .active {
                    background: #fff;
                    box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04),
                      0 1px 2px 0 rgba(0, 0, 0, 0.02);
                    color: #333;
                  }

                  .toggle-item {
                    border-radius: 999px;
                    background: transparent;
                    color: rgba(51, 51, 51, 0.6);

                    .icon-wrapper {
                      width: 24px;
                      height: 24px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      cursor: pointer;
                    }
                  }
                }
              }
            }
          }
        }

        .information-wrapper {
          -webkit-user-select: none;
          user-select: none;
          cursor: pointer;
          position: relative;
          margin-bottom: 20px;
          height: 48px;
          width: 100%;
          display: flex;
          font-weight: 600;
          align-items: center;
          border-radius: 999px;
        }
      }
    }

    .main-content {
      width: 100%;
    }

    .main-content {
      @media screen and (min-width: 960px) and (max-width: 1191px) {
        padding-left: calc(-6px + 25vw);
      }

      @media screen and (min-width: 1192px) and (max-width: 1423px) {
        padding-left: calc(-4.8px + 20vw);
      }

      @media screen and (min-width: 1424px) and (max-width: 1727px) {
        padding-left: calc(-5.33333px + 16.66667vw);
      }

      @media screen and (min-width: 1728px) {
        padding-left: 282.66667px;
      }
    }
  }
}
</style>
