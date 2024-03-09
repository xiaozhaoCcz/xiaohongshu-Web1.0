<template>
  <div class="container" id="container">
    <div v-if="isLogin" class="push-container" id="tagContainer">
      <div class="header"><span class="header-icon"></span><span class="header-title">发布图文</span></div>
      <div class="img-list">
        <el-upload
          v-model:file-list="fileList"
          action="http://localhost:88/api/util/oss/saveBatch/0"
          list-type="picture-card"
          multiple
          :limit="9"
          :headers="uploadHeader"
          :auto-upload="false"
        >
          <el-icon>
            <Plus />
          </el-icon>
        </el-upload>

        <el-dialog v-model="dialogVisible">
          <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
      </div>
      <el-divider style="margin: 12px; width: 576px" />
      <div class="push-content">
        <el-input
          v-model="note.title"
          maxlength="20"
          show-word-limit
          type="text"
          placeholder="请输入标题"
          class="input-title"
        />
        <p
          id="post-textarea"
          ref="postContent"
          class="post-content"
          contenteditable="true"
          data-tribute="true"
          placeholder="填写更全面的描述信息，让更多的人看到你吧！"
        ></p>

        <div v-infinite-scroll="loadMoreData" class="scroll-tag-container" v-show="showTagState">
          <p v-for="(item, index) in selectTagList" :key="index" class="scrollbar-tag-item" @click="selectTag(item)">
            {{ item.title }}
          </p>
        </div>
      </div>

      <div class="categorys">
        <el-cascader
          v-model="categoryList"
          :options="options"
          @change="handleChange"
          :props="props"
          placeholder="请选择分类"
        />
      </div>
      <div class="btns">
        <button class="css-fm44j css-osq2ks dyn">
          <span class="btn-content" @click="addTag"># 话题</span></button
        ><button class="css-fm44j css-osq2ks dyn">
          <span class="btn-content"><span>@</span> 用户</span></button
        ><button class="css-fm44j css-osq2ks dyn">
          <span class="btn-content">
            <div class="smile"></div>
            表情
          </span>
        </button>
      </div>

      <div class="submit">
        <el-button type="danger" loading :disabled="true" v-if="pushLoading">发布</el-button>
        <button class="publishBtn" @click="pubslish()" v-else>
          <span class="btn-content">发布</span>
        </button>
        <button class="clearBtn">
          <span class="btn-content">取消</span>
        </button>
      </div>
    </div>
    <div v-else>
      <el-empty description="用户未登录" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { Plus } from "@element-plus/icons-vue";
import { useRoute } from "vue-router";
import type { UploadUserFile, CascaderProps } from "element-plus";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/store/userStore";
import { getCategoryTreeData } from "@/api/category";
import { saveNoteByDTO, getNoteById, updateNoteByDTO } from "@/api/note";
import { getPageTagByKeyword } from "@/api/tag";
import { getFileFromUrl, getHtmlContent } from "@/utils/util";
// import Schema from "async-validator";
// import Crop from "@/components/Crop.vue";
const props: CascaderProps = {
  label: "title",
  value: "id",
};

// const rules = {
//   title: { required: true, message: "标题不能为空" },
//   content: { required: true, message: "内容不能为空" },
//   category: { required: true, message: "分类不能为空" },
// };
// const validator = new Schema(rules);

const userStore = useUserStore();
const route = useRoute();

const fileList = ref<UploadUserFile[]>([]);

const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const uploadHeader = ref({
  accessToken: userStore.getToken(),
});
const categoryList = ref<Array<any>>([]);
const options = ref([]);
const note = ref<any>({});
const showTagState = ref(false);
const selectTagList = ref<Array<any>>([]);
const currentPage = ref(1);
const pageSize = 10;
const tagTotal = ref(0);
const pushLoading = ref(false);
const isLogin = ref(false);
const postContent = ref(null);

// 监听外部点击
onMounted(() => {
  if (!isLogin.value) {
    return;
  }
  document.getElementById("container")!.addEventListener("click", function (e) {
    var event = e || window.event;
    var target = event.target || (event.srcElement as any);
    // if(target.id == "name") {
    if (document.getElementById("tagContainer")!.contains(target)) {
      console.log("in");
    } else {
      showTagState.value = false;
    }
  });

  // replace(/<[^>]*>[^<]*(<[^>]*>)?/gi,"")
  document.getElementById("post-textarea")!.addEventListener("input", () => {});
});

const addTag = () => {
  showTagState.value = true;
  selectTagList.value = [];
  currentPage.value = 1;
  setData();
};

const setData = () => {
  getPageTagByKeyword(currentPage.value, pageSize, "").then((res) => {
    const { records, total } = res.data;
    selectTagList.value.push(...records);
    tagTotal.value = total;
  });
};

const selectTag = (val: any) => {
  // content.value += val.title;
  const contentDom = document.getElementById("post-textarea");
  contentDom!.innerHTML += `<a href='#' style='text-decoration:none'>#${val.title}</a>`;
  console.log(contentDom!.innerHTML);
  showTagState.value = false;
};

const loadMoreData = () => {
  currentPage.value += 1;
  setData();
};

const handleChange = (ids: Array<any>) => {
  categoryList.value = ids;
};

const getNoteByIdMethod = (noteId: string) => {
  getNoteById(noteId).then((res) => {
    console.log("---edit", res.data);
    const { data } = res;
    note.value = data;
    const urls = JSON.parse(data.urls);
    console.log(urls);
    urls.forEach((item: string) => {
      const fileName = item.substring(item.lastIndexOf("/") + 1);
      console.log(fileName);
      getFileFromUrl(item, fileName).then((res: any) => {
        fileList.value.push({ name: fileName, url: item, raw: res });
      });
    });
    categoryList.value.push(data.cpid);
    categoryList.value.push(data.cid);
    document.getElementById("post-textarea")!.innerHTML += data.content;
    data.tagList.forEach((item: any) => {
      document.getElementById("post-textarea")!.innerHTML +=
        `<a href='#' style='text-decoration:none'>#${item.title}</a>`;
    });
  });
};

// 上传图片功能
const pubslish = () => {
  //验证
  if (fileList.value.length <= 0 || note.value.title === null || categoryList.value.length <= 0) {
    ElMessage.error("请选择图片，标签，分类～");
    return;
  }
  pushLoading.value = true;
  let params = new FormData();
  //注意此处对文件数组进行了参数循环添加

  fileList.value.forEach((file: any) => {
    params.append("uploadFiles", file.raw);
    console.log(file.raw);
  });

  note.value.count = fileList.value.length;
  note.value.type = 1;
  note.value.content = document.getElementById("post-textarea")!.innerHTML.replace(/<[^>]*>[^<]*(<[^>]*>)?/gi, "");
  note.value.cpid = categoryList.value[0];
  note.value.cid = categoryList.value[1];
  note.value.tagList = [];
  const _content = getHtmlContent(document.getElementById("post-textarea")!.innerHTML);
  console.log(_content);
  _content.forEach((item: string) => {
    note.value.tagList.push(item.replace("#", ""));
  });

  const coverImage = new Image();
  coverImage.src = fileList.value[0].url!;
  coverImage.onload = () => {
    const size = coverImage.width / coverImage.height;
    note.value.noteCoverHeight = size >= 1.3 ? 200 : 300;
    const noteData = JSON.stringify(note.value);
    params.append("noteData", noteData);

    if (note.value.id !== null && note.value.id !== undefined) {
      updateNote(params);
    } else {
      saveNote(params);
    }
  };
};

const updateNote = (params: FormData) => {
  updateNoteByDTO(params)
    .then(() => {
      resetData();
      ElMessage.success("修改笔记成功");
    })
    .catch(() => {
      ElMessage.error("修改图片失败");
    })
    .finally(() => {
      pushLoading.value = false;
    });
};

const saveNote = (params: FormData) => {
  saveNoteByDTO(params)
    .then(() => {
      resetData();
      ElMessage.success("发布笔记成功");
    })
    .catch(() => {
      ElMessage.error("发布图片失败");
    })
    .finally(() => {
      pushLoading.value = false;
    });
};

const resetData = () => {
  note.value = {};
  document.getElementById("post-textarea")!.innerHTML = "";
  categoryList.value = [];
  fileList.value = [];
  pushLoading.value = false;
};

const initData = () => {
  isLogin.value = userStore.isLogin();
  if (isLogin.value) {
    const noteId = route.query.noteId as string;
    console.log("---noteId", noteId);
    if (noteId !== "" && noteId !== undefined) {
      getNoteByIdMethod(noteId);
    }
    getCategoryTreeData().then((res) => {
      options.value = res.data;
    });
  }
};

initData();
</script>

<style lang="less" scoped>
:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 80px;
  height: 80px;
}

:deep(.el-upload-list__item.is-success .el-upload-list__item-status-label) {
  display: none;
}

:deep(.el-upload--picture-card) {
  width: 80px;
  height: 80px;
}

a {
  text-decoration: none;
}

.container {
  flex: 1;
  padding-top: 72px;

  .push-container {
    margin-left: 12vw;
    width: 600px;
    border-radius: 8px;
    box-sizing: border-box;
    box-shadow: var(--el-box-shadow-lighter);

    .header {
      padding: 15px 20px;
      line-height: 16px;
      font-size: 16px;
      font-weight: 400;

      .header-icon {
        position: relative;
        top: 2px;
        display: inline-block;
        width: 6px;
        height: 16px;
        background: #3a64ff;
        border-radius: 3px;
        margin-right: 2px;
      }
    }

    .img-list {
      width: 540px;
      margin: auto;
      padding: 0px 6px 0px 6px;
    }

    .push-content {
      padding: 0 12px 10px 12px;
      position: relative;

      .scroll-tag-container {
        position: absolute;
        width: 98%;
        background-color: #fff;
        z-index: 99999;
        border: 1px solid #f4f4f4;
        height: 300px;
        overflow: auto;

        .scrollbar-tag-item {
          display: flex;
          align-items: center;
          height: 30px;
          margin: 10px;
          text-align: center;
          border-radius: 4px;
          padding-left: 2px;
          color: #484848;
          font-size: 14px;
        }

        .scrollbar-tag-item:hover {
          background-color: #f8f8f8;
        }
      }

      .input-title {
        margin-bottom: 5px;
        font-size: 12px;
      }

      .input-content {
        font-size: 12px;
      }

      .post-content:empty::before {
        content: attr(placeholder);
        color: #ccc;
        font-size: 14px;
      }

      .post-content {
        cursor: text;
        margin-top: 10px;
        width: 100%;
        min-height: 90px;
        max-height: 300px;
        margin-bottom: 10px;
        background: #fff;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        padding: 6px 12px 22px;
        outline: none;
        overflow-y: auto;
        text-rendering: optimizeLegibility;
        font-size: 14px;
        line-height: 22px;
      }

      .post-content:focus,
      .post-content:hover {
        border: 1px solid #3a64ff;
      }
    }

    .btns {
      padding: 0 12px 10px 12px;

      button {
        min-width: 62px;
        width: 62px;
        margin: 0 6px 0 0;
        height: 18px;
      }

      .css-fm44j {
        -webkit-font-smoothing: antialiased;
        appearance: none;
        font-family:
          RedNum,
          RedZh,
          RedEn,
          -apple-system;
        vertical-align: middle;
        text-decoration: none;
        border: 1px solid rgb(217, 217, 217);
        outline: none;
        user-select: none;
        cursor: pointer;
        display: inline-flex;
        -webkit-box-pack: center;
        justify-content: center;
        -webkit-box-align: center;
        align-items: center;
        margin-right: 16px;
        border-radius: 4px;
        background-color: white;
        color: rgb(38, 38, 38);
        height: 24px;
        font-size: 12px;
      }
    }

    .categorys {
      padding: 0 12px 10px 12px;
    }

    .submit {
      padding: 0 12px 10px 12px;
      margin-top: 10px;

      button {
        width: 80px;
        height: 32px;
        font-size: 14px;
      }

      .publishBtn {
        background-color: #ff2442;
        color: #fff;
        border-radius: 4px;
      }

      .clearBtn {
        border-radius: 4px;
        margin-left: 10px;
        border: 1px solid rgb(217, 217, 217);
      }
    }
  }
}
</style>
