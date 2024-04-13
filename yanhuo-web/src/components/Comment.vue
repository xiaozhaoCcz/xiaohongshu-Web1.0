<template>
  <div class="comments-container">
    <div class="total">共{{ computedTotal }}条评论</div>
    <div class="list-container">
      <div class="parent-comment" v-for="(oneComment, oneIndex) in dataList" :key="oneIndex">
        <div class="comment-item">
          <div class="comment-inner-container">
            <div class="avatar">
              <img class="avatar-item" :src="oneComment.avatar" />
            </div>
            <div class="right">
              <div class="author-wrapper">
                <div class="author">
                  <a class="name">{{ oneComment.username }}</a>
                </div>
              </div>
              <div class="content">{{ oneComment.content }}</div>

              <div class="info">
                <div class="date">
                  <span>{{ oneComment.time }}</span>
                </div>
                <div class="interactions">
                  <div class="like">
                    <span
                      class="like-wrapper"
                      v-if="oneComment.isLike"
                      @click="likeComment(oneComment, -1, oneIndex, -1)"
                    >
                      <i class="iconfont icon-follow-fill" style="width: 1em; height: 1em"></i>
                      <span class="count">{{ oneComment.likeCount }}</span>
                    </span>
                    <span class="like-wrapper" v-else @click="likeComment(oneComment, 1, oneIndex, -1)">
                      <i class="iconfont icon-follow" style="width: 1em; height: 1em"></i>
                      <span class="count">{{ oneComment.likeCount }}</span>
                    </span>
                  </div>
                  <div class="reply" @click="saveComment(oneComment, oneIndex, 0)">
                    <span class="like-wrapper">
                      <ChatRound style="width: 1.2em; height: 1.2em" />
                      <span class="count">{{ oneComment.twoCommentCount }}</span>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="reply-container">
          <div class="list-container">
            <div class="comment-item" v-for="(twoComment, twoIndex) in oneComment.children" :key="twoIndex">
              <div class="comment-inner-container">
                <div class="avatar">
                  <img class="avatar-item" :src="twoComment.avatar" />
                </div>
                <div class="right">
                  <div class="author-wrapper">
                    <div class="author">
                      <a class="name">{{ twoComment.username }}</a>
                    </div>
                  </div>
                  <div class="content">
                    回复<span style="color: rgba(61, 61, 61, 0.8)">{{ twoComment.replyUsername }}: </span
                    >{{ twoComment.content }}
                  </div>

                  <div class="info">
                    <div class="date">
                      <span>{{ twoComment.time }}</span>
                    </div>
                    <div class="interactions">
                      <div class="like">
                        <span
                          class="like-wrapper"
                          v-if="twoComment.isLike"
                          @click="likeComment(twoComment, -1, oneIndex, twoIndex)"
                        >
                          <i class="iconfont icon-follow-fill" style="width: 1em; height: 1em"></i>
                          <span class="count">{{ twoComment.likeCount }}</span>
                        </span>
                        <span class="like-wrapper" @click="likeComment(twoComment, 1, oneIndex, twoIndex)" v-else>
                          <i class="iconfont icon-follow" style="width: 1em; height: 1em"></i>
                          <span class="count">{{ twoComment.likeCount }}</span>
                        </span>
                      </div>
                      <div class="reply" @click="saveComment(twoComment, oneIndex, twoIndex)">
                        <span class="like-wrapper">
                          <ChatRound style="width: 1.2em; height: 1.2em" />
                          <span class="count">回复</span>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div
            class="show-more"
            v-if="
              oneComment.twoCommentCount >= commentTotalMap.get(oneComment.id) &&
              oneComment.twoCommentCount > showTwoCommentCount
            "
            @click="loadTwoMore(oneComment.id, oneIndex)"
          >
            展开更多的回复
          </div>
          <div
            class="show-more"
            v-if="
              oneComment.twoCommentCount < commentTotalMap.get(oneComment.id) &&
              oneComment.twoCommentCount > showTwoCommentCount
            "
            @click="reback(oneComment.id, oneIndex)"
          >
            收起所有回复
          </div>
        </div>
      </div>
    </div>
    <div style="padding-bottom: 100px"></div>
  </div>
</template>
<script lang="ts" setup>
import { ChatRound } from "@element-plus/icons-vue";
import { ref, watch } from "vue";
import { getCommentPageWithCommentByNoteId, getTwoCommentPageByOneCommentId } from "@/api/comment";
import { likeOrCollectionByDTO } from "@/api/likeOrCollection";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";
import { formateTime } from "@/utils/util";
const props = defineProps({
  nid: {
    type: String,
    default: "",
  },
  replyComment: {
    type: Object,
    // eslint-disable-next-line vue/require-valid-default-prop
    default: {},
  },
  currentPage: {
    type: Number,
    default: 1,
  },
  seed: {
    type: String,
    default: "",
  },
});
const emit = defineEmits(["clickComment"]);

// const currentPage = ref(1);

const dataList = ref<Array<any>>([]);
const commentTotal = ref(0);
const computedTotal = ref(0);
const oneIndex = ref(-1);
const twoIndex = ref(-1);

const pageSize = 7;
const twoPageSize = 10;
const showTwoCommentCount = 3;
const commentMap = new Map();
const commentTotalMap = new Map();

const likeComment = (comment: any, status: number, one: number, two: number) => {
  const data = {} as LikeOrCollectionDTO;
  data.likeOrCollectionId = comment.id;
  data.publishUid = comment.uid;
  data.type = 2;
  likeOrCollectionByDTO(data).then(() => {
    if (two === -1) {
      dataList.value[one].isLike = status == 1;
      dataList.value[one].likeCount += status;
    } else {
      dataList.value[one].children[two].isLike = status == 1;
      dataList.value[one].children[two].likeCount += status;
    }
  });
};

const saveComment = (comment: any, one: number, two: number) => {
  oneIndex.value = one;
  twoIndex.value = two;
  emit("clickComment", comment);
};

const addComment = () => {
  //   if (props.replyComment.pid === undefined) return;

  let comment = props.replyComment;
  console.log("comment", comment);
  comment.likeCount = 0;
  comment.twoCommentCount = 0;
  comment.time = formateTime(new Date().getTime());
  if (comment.pid === "0") {
    dataList.value.splice(0, 0, comment);
  } else {
    if (dataList.value[oneIndex.value].children == null) {
      dataList.value[oneIndex.value].children = [];
    }
    dataList.value[oneIndex.value].children.splice(twoIndex.value + 1, 0, comment);
  }
  computedTotal.value += 1;
};

const loadTwoMore = (oneCommentId: string, index: number) => {
  let page = commentMap.get(oneCommentId);
  page += 1;
  getTwoCommentPageByOneCommentId(page, twoPageSize, oneCommentId).then((res) => {
    const { records } = res.data;
    if (page === 1) {
      const spliceData = records.splice(showTwoCommentCount, records.length);
      spliceData.forEach((item: any) => {
        item.time = formateTime(item.time);
        dataList.value[index].children.push(item);
      });
    } else {
      records.forEach((item: any) => {
        item.time = formateTime(item.time);
        dataList.value[index].children.push(item);
      });
    }
    commentTotalMap.set(oneCommentId, commentTotalMap.get(oneCommentId) + twoPageSize);
    commentMap.set(oneCommentId, page);
  });
};

const reback = (oneCommentId: string, index: number) => {
  commentTotalMap.set(oneCommentId, 0);
  commentMap.set(oneCommentId, 0);
  const twoSpliceComment = dataList.value[index].children.splice(0, showTwoCommentCount);
  dataList.value[index].children = twoSpliceComment;
};

const getCommentData = () => {
  computedTotal.value = 0;
  getCommentPageWithCommentByNoteId(props.currentPage, pageSize, props.nid).then((res: any) => {
    const { records, total } = res.data;
    records.forEach((item: any) => {
      item.time = formateTime(item.time);
      const twoComments = item.children;
      // 设置每一个一级评论的集合
      commentMap.set(item.id, 0);
      commentTotalMap.set(item.id, 0);
      if (twoComments != null) {
        const twoData = [] as Array<any>;
        twoComments.forEach((element: any) => {
          element.time = formateTime(element.time);
          twoData.push(element);
        });

        item.children = twoData;
      }
      computedTotal.value += item.twoCommentCount + 1;
      dataList.value.push(item);
    });
    console.log("---所有评论", dataList.value);
    commentTotal.value = total;
    if (pageSize * props.currentPage >= commentTotal.value) return;
  });
};

watch(
  () => [props.nid, props.seed, props.currentPage],
  ([newNid, newSeed], [oldNid, oldSeed]) => {
    console.log("评论功能", newNid, oldNid, props.currentPage);
    if (newNid !== oldNid) {
      dataList.value = [];
      getCommentData();
    }
    if (newSeed !== oldSeed) {
      addComment();
    }
  }
);
</script>
<style lang="less" scoped>
.comments-container {
  padding: 16px;

  .total {
    font-size: 14px;
    color: rgba(51, 51, 51, 0.6);
    margin-left: 8px;
    margin-bottom: 12px;
  }

  .list-container {
    position: relative;

    .parent-comment {
      margin-bottom: 16px;

      .comment-item {
        position: relative;
        display: flex;
        padding: 8px;

        .comment-inner-container {
          position: relative;
          display: flex;
          z-index: 1;
          width: 100%;
          flex-shrink: 0;

          .avatar {
            flex: 0 0 auto;

            .avatar-item {
              display: flex;
              align-items: center;
              justify-content: center;
              cursor: pointer;
              border-radius: 100%;
              border: 1px solid rgba(0, 0, 0, 0.08);
              object-fit: cover;
              width: 40px;
              height: 40px;
            }
          }

          .right {
            margin-left: 12px;
            display: flex;
            flex-direction: column;
            font-size: 14px;
            flex-grow: 1;

            .author-wrapper {
              display: flex;
              justify-content: space-between;
              align-items: center;

              .author {
                display: flex;
                align-items: center;

                .name {
                  color: rgba(51, 51, 51, 0.6);
                  line-height: 18px;
                }
              }
            }

            .content {
              margin-top: 4px;
              line-height: 140%;
              color: #333;
            }

            .info {
              display: flex;
              flex-direction: column;
              justify-content: space-between;
              font-size: 12px;
              line-height: 16px;
              color: rgba(51, 51, 51, 0.6);

              .date {
                margin: 8px 0;
              }

              .interactions {
                display: flex;
                margin-left: -2px;

                .like-wrapper {
                  padding: 0 4px;
                  color: rgba(51, 51, 51, 0.8);
                  font-weight: 500;

                  position: relative;
                  cursor: pointer;
                  display: flex;
                  align-items: center;

                  .like-lottie {
                    width: 16px;
                    height: 16px;
                    left: 4px;
                  }

                  .count {
                    margin-left: 2px;
                    font-weight: 500;
                  }
                }
              }
            }
          }
        }
      }

      .reply-container {
        margin-left: 52px;

        .show-more {
          margin-left: 44px;
          height: 32px;
          line-height: 32px;
          color: #13386c;
          cursor: pointer;
          font-weight: 500;
          font-size: 14px;
        }
      }
    }
  }
}
</style>
