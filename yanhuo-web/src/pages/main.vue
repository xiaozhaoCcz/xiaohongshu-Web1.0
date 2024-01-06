<template>
  <div
    class="note-detail-mask"
    style="transition: background-color 0.4s ease 0s;
  hsla(0,0%,100%,0.98)"
  >
    <div class="note-container">
      <div class="media-container">
        <el-carousel height="90vh">
          <el-carousel-item v-for="(item, index) in noteInfo.imgList" :key="index">
            <el-image style="width: 100%; height: 100%" :src="item" fit="contain" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="interaction-container">
        <div class="author-container">
          <div class="author-me">
            <div class="info">
              <img
                class="avatar-item"
                style="width: 40px; height: 40px"
                :src="noteInfo.avatar"
              />
              <span class="name">{{ noteInfo.username }}</span>
            </div>
            <div class="follow-btn">
              <el-button
                type="info"
                size="large"
                round
                v-if="followerState"
                @click="follow(noteInfo.uid, 1)"
                >已关注</el-button
              >
              <el-button
                type="danger"
                size="large"
                round
                v-else
                @click="follow(noteInfo.uid, 0)"
                >关注</el-button
              >
            </div>
          </div>

          <div class="note-scroller">
            <div class="note-content">
              <div class="title">{{ noteInfo.title }}</div>
              <div class="desc">
                <span>{{ noteInfo.content }} <br /></span>
                <a
                  class="tag tag-search"
                  v-for="(item, index) in noteInfo.tagList"
                  :key="index"
                  >#{{ item.title }}</a
                >
              </div>
              <div class="bottom-container">
                <span class="date">{{ noteInfo.time }}</span>
              </div>
            </div>
            <div class="divider interaction-divider"></div>

            <!-- 评论 -->

            <div class="comments-el">
              <div class="comments-container">
                <div class="total">共63条评论</div>
                <div class="list-container">
                  <div class="parent-comment">
                    <div class="comment-item">
                      <div class="comment-inner-container">
                        <div class="avatar">
                          <img
                            class="avatar-item"
                            src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg"
                          />
                        </div>
                        <div class="right">
                          <div class="author-wrapper">
                            <div class="author"><a class="name">用户已注销</a></div>
                          </div>
                          <div class="content">内容</div>

                          <div class="info">
                            <div class="date"><span>昨天 16:49</span></div>
                            <div class="interactions">
                              <div class="like">
                                <span class="like-wrapper">
                                  <Star style="width: 1em; height: 1em" />
                                  <span class="count">247</span>
                                </span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="reply-container">
                      <div class="list-container">
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <Star style="width: 1em; height: 1em" />
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <Star style="width: 1em; height: 1em" />
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <Star style="width: 1em; height: 1em" />
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>

                      <div class="show-more">展开更多的回复</div>
                    </div>
                  </div>
                  <div class="parent-comment">
                    <div class="comment-item">
                      <div class="comment-inner-container">
                        <div class="avatar">
                          <img
                            class="avatar-item"
                            src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg"
                          />
                        </div>
                        <div class="right">
                          <div class="author-wrapper">
                            <div class="author"><a class="name">用户已注销</a></div>
                          </div>
                          <div class="content">内容</div>

                          <div class="info">
                            <div class="date"><span>昨天 16:49</span></div>
                            <div class="interactions">
                              <div class="like">
                                <span class="like-wrapper">
                                  <span class="count">247</span>
                                </span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="reply-container">
                      <div class="list-container">
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="comment-item">
                          <div class="comment-inner-container">
                            <div class="avatar">
                              <img
                                class="avatar-item"
                                src="https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg"
                              />
                            </div>
                            <div class="right">
                              <div class="author-wrapper">
                                <div class="author"><a class="name">用户已注销</a></div>
                              </div>
                              <div class="content">内容</div>

                              <div class="info">
                                <div class="date"><span>昨天 16:49</span></div>
                                <div class="interactions">
                                  <div class="like">
                                    <span class="like-wrapper">
                                      <span class="count">247</span>
                                    </span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>

                      <div class="show-more">展开更多的回复</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!--  -->
          </div>

          <div class="interactions-footer">
            <div class="buttons">
              <div class="left">
                <span class="like-wrapper"
                  ><span class="like-lottie">
                    <Star style="width: 0.8em; height: 0.8em; color: #333" /> </span
                  ><span class="count">{{ noteInfo.collectionCount }}</span></span
                >
                <span class="collect-wrapper">
                  <span class="like-lottie">
                    <i
                      class="iconfont icon-follow"
                      style="width: 0.8em; height: 0.8em; color: #333"
                    ></i> </span
                  ><span class="count">{{ noteInfo.likeCount }}</span></span
                >
                <span class="chat-wrapper">
                  <span class="like-lottie">
                    <ChatRound style="width: 0.8em; height: 0.8em; color: #333" /> </span
                  ><span class="count">{{ noteInfo.commentCount }}</span></span
                >
              </div>
              <div class="share-wrapper"></div>
            </div>
            <div class="comment-wrapper active comment-comp">
              <div class="input-wrapper">
                <input class="comment-input" type="text" placeholder="回复内容" />
                <div class="input-buttons">
                  <Close style="width: 1.2em; height: 1.2em" />
                </div>
              </div>
              <button class="submit">发送</button>
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
import { Close, Star, ChatRound } from "@element-plus/icons-vue";
import { ref, watchEffect } from "vue";
import { getNoteById } from "@/api/note";
import type { NoteInfo } from "@/type/note";
import { formateTime } from "@/utils/util";
import { isFollow, followById } from "@/api/follower";

// 这是路由传参
// nid.value = history.state.nid;
// watch(
//   [props.nid],
//   () => {
//     console.log("发生改变");
//   },
//   {
//     deep: true,
//     immediate: true,
//   }
// );
const emit = defineEmits(["clickMain"]);
const close = () => {
  emit("clickMain", false);
};

const props = defineProps({
  nid: {
    type: String,
    default: "",
  },
});

const noteInfo = ref<NoteInfo>({});
const followerState = ref(false);

const follow = (fid: string, type: number) => {
  followById(fid).then((res) => {
    console.log("---关注", res.data);
    followerState.value = type == 0;
  });
};

watchEffect(() => {
  const p = new Promise((resolve) => {
    getNoteById(props.nid).then((res: any) => {
      const imgList = JSON.parse(res.data.urls);
      const time = formateTime(res.data.time);
      noteInfo.value = res.data;
      noteInfo.value.imgList = imgList;
      noteInfo.value.time = time;
      resolve(res.data.uid);
    });
  });

  p.then((data) => {
    console.log("data", data);
    isFollow(data).then((res) => {
      followerState.value = res.data;
    });
  });
});
</script>

<style lang="less" scoped>
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
      box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04), 0 1px 2px 0 rgba(0, 0, 0, 0.02);
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
    transition: transform 0.4s ease 0s, width 0.4s ease 0s;
    transform: translate(104px, 32px) scale(1);
    overflow: visible;

    display: flex;
    box-shadow: 0 8px 64px 0 rgba(0, 0, 0, 0.04), 0 1px 4px 0 rgba(0, 0, 0, 0.02);
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
