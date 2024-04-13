<template>
  <div
    class="note-detail-mask"
    style="transition: background-color 0.4s ease 0s;
  hsla(0,0%,100%,0.98)"
  >
    <div class="note-container">
      <div class="media-container">
        <el-carousel height="90vh" :autoplay="false">
          <el-carousel-item v-for="(item, index) in noteInfo.imgList" :key="index">
            <el-image
              style="width: 100%; height: 100%"
              :src="item"
              fit="contain"
              class="animate__animated animate__zoomIn animate__delay-0.5s"
            />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="interaction-container">
        <div class="author-container">
          <div class="author-me">
            <div class="info" @click="toUser(noteInfo.uid)">
              <img class="avatar-item" style="width: 40px; height: 40px" :src="noteInfo.avatar" />
              <span class="name">{{ noteInfo.username }}</span>
            </div>
            <div class="follow-btn" v-if="currentUid !== noteInfo.uid">
              <el-button type="info" size="large" round v-if="noteInfo.isFollow" @click="follow(noteInfo.uid, 1)"
                >已关注</el-button
              >
              <el-button type="danger" size="large" round v-else @click="follow(noteInfo.uid, 0)">关注</el-button>
            </div>
            <div class="follow-btn" v-else>
              <el-dropdown>
                <el-button type="danger" size="large" round>
                  编辑<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item v-if="noteInfo.pinned === 0" @click="pinned(noteInfo.id, 1)"
                      >置顶</el-dropdown-item
                    >
                    <el-dropdown-item v-else @click="pinned(noteInfo.id, 0)">取消置顶</el-dropdown-item>
                    <el-dropdown-item @click="deleteNote(noteInfo.id)">删除</el-dropdown-item>
                    <el-dropdown-item @click="toEdit(noteInfo.id)">编辑</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>

          <div class="note-scroller" ref="noteScroller" @scroll="loadMoreData">
            <div class="note-content">
              <div class="title">{{ noteInfo.title }}</div>
              <div class="desc">
                <span>{{ noteInfo.content }} <br /></span>
                <a class="tag tag-search" v-for="(item, index) in noteInfo.tagList" :key="index">#{{ item.title }}</a>
              </div>
              <div class="bottom-container">
                <span class="date">{{ noteInfo.time }}</span>
              </div>
            </div>
            <div class="divider interaction-divider"></div>

            <!-- 评论 -->
            <div class="comments-el">
              <Comment
                :nid="props.nid"
                :currentPage="currentPage"
                :replyComment="replyComment"
                :seed="seed"
                @click-comment="clickComment"
              ></Comment>
            </div>
            <!--  -->
          </div>

          <div class="interactions-footer">
            <div class="buttons">
              <div class="left">
                <span class="like-wrapper"
                  ><span class="like-lottie" v-if="noteInfo.isCollection" @click="likeOrCollection(3, -1)">
                    <StarFilled style="width: 0.9em; height: 0.9em; color: #333" />
                  </span>
                  <span class="like-lottie" v-else @click="likeOrCollection(3, 1)">
                    <Star style="width: 0.8em; height: 0.8em; color: #333" />
                  </span>
                  <span class="count">{{ noteInfo.collectionCount }}</span></span
                >
                <span class="collect-wrapper">
                  <span class="like-lottie" v-if="noteInfo.isLike" @click="likeOrCollection(1, -1)">
                    <i class="iconfont icon-follow-fill" style="width: 0.8em; height: 0.8em; color: #333"></i>
                  </span>
                  <span class="like-lottie" v-else @click="likeOrCollection(1, 1)">
                    <i class="iconfont icon-follow" style="width: 0.8em; height: 0.8em; color: #333"></i>
                  </span>
                  <span class="count">{{ noteInfo.likeCount }}</span></span
                >
                <span class="chat-wrapper">
                  <span class="like-lottie"> <ChatRound style="width: 0.8em; height: 0.8em; color: #333" /> </span
                  ><span class="count">{{ noteInfo.commentCount }}</span></span
                >
              </div>
              <div class="share-wrapper"></div>
            </div>
            <div :class="showSaveBtn ? 'comment-wrapper active comment-comp ' : 'comment-wrapper comment-comp '">
              <div class="input-wrapper">
                <input
                  class="comment-input"
                  v-model="commentValue"
                  type="text"
                  :placeholder="commentPlaceVal"
                  @input="commenInput"
                />
                <div class="input-buttons" @click="clearCommeent" v-show="showSaveBtn">
                  <Close style="width: 1.2em; height: 1.2em" />
                </div>
              </div>
              <button class="submit" @click="saveComment">发送</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="close-cricle" @click="close">
      <div class="close close-mask-white">
        <Close style="width: 1.2em; height: 1.2em; color: rgba(51, 51, 51, 0.8)" />
      </div>
    </div>

    <div class="back-desk"></div>
  </div>
</template>

<script lang="ts" setup>
import { Close, Star, ChatRound, StarFilled, ArrowDown } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { ref, watch } from "vue";
import { getNoteById, pinnedNote, deleteNoteByIds } from "@/api/note";
import { likeOrCollectionByDTO } from "@/api/likeOrCollection";
import type { NoteInfo } from "@/type/note";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";
import { formateTime, getRandomString } from "@/utils/util";
import { followById } from "@/api/follower";
import Comment from "@/components/Comment.vue";
import type { CommentDTO } from "@/type/comment";
import { saveCommentByDTO, syncCommentByIds } from "@/api/comment";
import { useRouter } from "vue-router";
import { useUserStore } from "@/store/userStore";
const userStore = useUserStore();
const router = useRouter();

// 这是路由传参
// nid.value = history.state.nid;

const emit = defineEmits(["clickMain"]);

const props = defineProps({
  nid: {
    type: String,
    default: "",
  },
  nowTime: {
    type: Date,
    default: null,
  },
});

const currentUid = ref("");
const noteInfo = ref<NoteInfo>({
  id: "",
  title: "",
  content: "",
  noteCover: "",
  uid: "",
  username: "",
  avatar: "",
  imgList: [],
  type: -1,
  likeCount: 0,
  collectionCount: 0,
  commentCount: 0,
  tagList: [],
  time: "",
  isFollow: false,
  isLike: false,
  isCollection: false,
  pinned: 0,
});
const commentValue = ref("");
const commentPlaceVal = ref("请输入内容");
const commentObject = ref<any>({});
const replyComment = ref<any>({});
const showSaveBtn = ref(false);
const currentPage = ref(1);
const seed = ref("");
const commentIds = ref<Array<string>>([]);
const noteScroller = ref(null);
const isLogin = ref(false);
const likeOrComment = ref({
  isLike: false,
  isComment: false,
});

watch(
  () => [props.nowTime],
  () => {
    currentPage.value = 1;
    if (props.nid !== null && props.nid !== "") {
      getNoteById(props.nid).then((res: any) => {
        console.log("---note", res.data);
        noteInfo.value = res.data;
        noteInfo.value.imgList = JSON.parse(res.data.urls);
        noteInfo.value.time = formateTime(res.data.time);
        likeOrComment.value.isLike = noteInfo.value.isLike;
      });
    }
  }
);

const noLoginNotice = () => {
  if (!isLogin.value) {
    ElMessage.warning("用户未登录");
    return false;
  }
  return true;
};

const toUser = (uid: string) => {
  const _login = noLoginNotice();
  if (!_login) {
    return;
  }
  router.push({ name: "user", query: { uid: uid } });
};

const close = () => {
  if (isLogin.value) {
    syncCommentByIds(commentIds.value).then(() => {
      commentIds.value = [];
      emit("clickMain", props.nid, likeOrComment.value);
    });
  } else {
    emit("clickMain");
  }
};

const follow = (fid: string, type: number) => {
  const _login = noLoginNotice();
  if (!_login) {
    return;
  }
  followById(fid).then(() => {
    noteInfo.value.isFollow = type == 0;
  });
};

const likeOrCollection = (type: number, val: number) => {
  const _login = noLoginNotice();
  if (!_login) {
    return;
  }
  const likeOrCollectionDTO = {} as LikeOrCollectionDTO;
  likeOrCollectionDTO.likeOrCollectionId = noteInfo.value.id;
  likeOrCollectionDTO.publishUid = noteInfo.value.uid;
  likeOrCollectionDTO.type = type == 1 ? 1 : 3;
  likeOrCollectionByDTO(likeOrCollectionDTO).then(() => {
    if (type == 1) {
      noteInfo.value.isLike = val == 1;
      noteInfo.value.likeCount += val;
      likeOrComment.value.isLike = val == 1;
    } else {
      noteInfo.value.isCollection = val == 1;
      noteInfo.value.collectionCount += val;
    }
  });
};

const pinned = (noteId: string, type: number) => {
  pinnedNote(noteId)
    .then((res: any) => {
      if (res.data) {
        noteInfo.value.pinned = type;
      }
    })
    .catch(() => {
      ElMessage.error("最多只能置顶2个笔记");
    });
};

const deleteNote = (noteId: string) => {
  const data = [] as Array<string>;
  data.push(noteId);
  deleteNoteByIds(data).then(() => {
    ElMessage.success("删除成功");
    emit("clickMain");
  });
};

const toEdit = (noteId: string) => {
  router.push({ path: "/push", query: { date: Date.now(), noteId: noteId } });
};

const clickComment = (comment: any) => {
  commentObject.value = comment;
  commentPlaceVal.value = "回复" + comment.username;
};

const commenInput = (e: any) => {
  const { value } = e.target;
  commentValue.value = value;
  showSaveBtn.value = commentValue.value.length > 0 || commentObject.value.pid !== undefined;
};

const saveComment = () => {
  const _login = noLoginNotice();
  if (!_login) {
    return;
  }
  const comment = {} as CommentDTO;
  comment.nid = props.nid;
  comment.noteUid = noteInfo.value.uid;
  if (commentObject.value.pid === undefined) {
    comment.pid = "0";
    comment.replyId = "0";
    comment.replyUid = noteInfo.value.uid;
    comment.level = 1;
  } else if (commentObject.value.pid == "0") {
    comment.pid = commentObject.value.id;
    comment.replyId = commentObject.value.id;
    comment.replyUid = commentObject.value.uid;
    comment.level = 2;
  } else {
    comment.pid = commentObject.value.pid;
    comment.replyId = commentObject.value.id;
    comment.replyUid = commentObject.value.uid;
    comment.level = 2;
  }

  comment.content = commentValue.value;
  saveCommentByDTO(comment).then((res: any) => {
    replyComment.value = res.data;
    replyComment.value.replyUsername = commentObject.value.username;
    commentValue.value = "";
    commentObject.value = {};
    commentPlaceVal.value = "请输入内容";
    showSaveBtn.value = false;
    seed.value = getRandomString(12);
    commentIds.value.push(res.data.id);
    likeOrComment.value.isComment = true;
  });
};

const clearCommeent = () => {
  commentValue.value = "";
  commentObject.value = {};
  commentPlaceVal.value = "请输入内容";
  showSaveBtn.value = false;
};

const loadMoreData = () => {
  console.log("main加载更多");
  const container = noteScroller.value as any;
  if (container.scrollTop + container.clientHeight >= container.scrollHeight) {
    currentPage.value += 1;
    console.log("到底了");
  }
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
:deep(.el-dropdown-menu__item:not(.is-disabled):focus) {
  background-color: #f8f8f8;
  color: black;
}

.note-detail-mask {
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  width: 100vw;
  height: 100vh;
  z-index: 20;
  overflow: auto;

  .back-desk {
    position: fixed;
    background-color: #f4f4f4;
    opacity: 0.5;
    width: 100vw;
    height: 100vh;
    z-index: 30;
  }

  .close-cricle {
    left: 1.3vw;
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

  .note-container {
    width: 86%;
    height: 90%;
    transition:
      transform 0.4s ease 0s,
      width 0.4s ease 0s;
    transform: translate(104px, 32px) scale(1);
    overflow: visible;

    display: flex;
    box-shadow:
      0 8px 64px 0 rgba(0, 0, 0, 0.04),
      0 1px 4px 0 rgba(0, 0, 0, 0.02);
    border-radius: 20px;
    background: #f8f8f8;
    transform-origin: left top;
    z-index: 100;

    .media-container {
      width: 68%;
      height: auto;

      position: relative;
      background: rgba(0, 0, 0, 0.03);
      flex-shrink: 0;
      flex-grow: 0;
      -webkit-user-select: none;
      user-select: none;
      overflow: hidden;
      border-radius: 20px 0 0 20px;
      transform: translateZ(0);
      height: 100%;
      object-fit: contain;
      min-width: 440px;
    }

    .interaction-container {
      width: 32%;
      flex-shrink: 0;
      border-radius: 0 20px 20px 0;
      position: relative;
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      height: 100%;
      background-color: #fff;
      overflow: hidden;
      border-left: 1px solid rgba(0, 0, 0, 0.08);

      .author-me {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 100%;
        padding: 24px;
        border-bottom: 1px solid transparent;

        .info {
          display: flex;
          align-items: center;

          .avatar-item {
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            border-radius: 100%;
            border: 1px solid rgba(0, 0, 0, 0.08);
            object-fit: cover;
          }

          .name {
            padding-left: 12px;
            height: 40px;
            display: flex;
            align-items: center;
            font-size: 16px;
            color: rgba(51, 51, 51, 0.8);
          }
        }
      }

      .note-scroller::-webkit-scrollbar {
        display: none;
      }

      .note-scroller {
        transition: scroll 0.4s;
        overflow-y: scroll;
        flex-grow: 1;
        height: 80vh;

        .note-content {
          padding: 0 24px 24px;
          color: var(--color-primary-label);

          .title {
            margin-bottom: 8px;
            font-weight: 600;
            font-size: 18px;
            line-height: 140%;
          }

          .desc {
            margin: 0;
            font-weight: 400;
            font-size: 16px;
            line-height: 150%;
            color: #333;
            white-space: pre-wrap;
            overflow-wrap: break-word;

            .tag-search {
              cursor: pointer;
            }

            .tag {
              margin-right: 2px;
              color: #13386c;
            }
          }

          .bottom-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 12px;

            .date {
              font-size: 14px;
              line-height: 120%;
              color: rgba(51, 51, 51, 0.6);
            }
          }
        }

        .interaction-divider {
          margin: 0 24px;
        }

        .divider {
          margin: 4px 8px;
          list-style: none;
          height: 0;
          border: solid rgba(0, 0, 0, 0.08);
          border-width: 1px 0 0;
        }

        .comments-el {
          position: relative;
        }
      }

      .interactions-footer {
        position: absolute;
        bottom: 0px;
        background: #fff;
        flex-shrink: 0;
        padding: 12px 24px 24px;
        height: 130px;
        border-top: 1px solid rgba(0, 0, 0, 0.08);
        flex-basis: 130px;
        z-index: 1;

        .buttons {
          display: flex;
          justify-content: space-between;

          .count {
            margin-left: 6px;
            margin-right: 12px;
            font-weight: 500;
            font-size: 12px;
          }

          .left {
            display: flex;

            .like-wrapper {
              position: relative;
              cursor: pointer;
              display: flex;
              justify-content: left;
              color: rgba(51, 51, 51, 0.6);
              margin-right: 5px;
              align-items: center;

              .like-lottie {
                transform: scale(1.7);
              }
            }

            .collect-wrapper {
              position: relative;
              cursor: pointer;
              display: flex;
              color: rgba(51, 51, 51, 0.6);
              margin-right: 5px;
              align-items: center;

              .like-lottie {
                transform: scale(1.7);
              }
            }

            .chat-wrapper {
              cursor: pointer;
              color: rgba(51, 51, 51, 0.6);
              display: flex;
              align-items: center;

              .like-lottie {
                transform: scale(1.7);
              }
            }
          }
        }

        .comment-wrapper {
          &.active {
            .input-wrapper {
              flex-shrink: 1;
            }
          }
        }

        .comment-wrapper {
          display: flex;
          font-size: 16px;
          overflow: hidden;

          .input-wrapper {
            display: flex;
            position: relative;
            width: 100%;
            flex-shrink: 0;
            transition: flex 0.3s;

            .comment-input:placeholder-shown {
              background-image: none;
              padding: 12px 92px 12px 36px;
              background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAANlBMVEUAAAA0NDQyMjIzMzM2NjY2NjYyMjI0NDQ1NTU1NTUzMzM1NTU1NTUzMzM1NTUzMzM1NTU1NTVl84gVAAAAEnRSTlMAmUyGEzlgc2AmfRx9aToKQzCSoXt+AAAAhElEQVRIx+3Uuw6DMAyF4XOcBOdCafv+L9vQkQFyJBak/JOHT7K8GLM7epuHusRhHwP/mejJ77i32CpZh33aD+lDFDzgZFE8+tgUv5BB9NxEb9NPL3i46JvoUUhXPBKZFQ/rTPHI3ZXt8xr12KX055LoAVtXz9kKHprxNMMxXqRvmAn9ACQ7A/tTXYAxAAAAAElFTkSuQmCC);
              background-repeat: no-repeat;
              background-size: 16px 16px;
              background-position: 16px 12px;
              color: rgba(51, 51, 51, 0.3);
            }

            .comment-input {
              padding: 12px 92px 12px 16px;
              width: 100%;
              height: 40px;
              line-height: 16px;
              background: rgba(0, 0, 0, 0.03);
              caret-color: rgba(51, 51, 51, 0.3);
              border-radius: 22px;
              border: none;
              outline: none;
              resize: none;
              color: #333;
            }

            .input-buttons {
              position: absolute;
              right: 0;
              top: 0;
              height: 40px;
              display: flex;
              align-items: center;
              justify-content: center;
              width: 92px;
              color: rgba(51, 51, 51, 0.3);
            }
          }

          .submit {
            margin-left: 8px;
            width: 60px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            flex-shrink: 0;
            background: #3d8af5;
            border-radius: 44px;
            font-size: 16px;
          }
        }

        .comment-comp {
          margin-top: 20px;
        }
      }
    }
  }
}
</style>
