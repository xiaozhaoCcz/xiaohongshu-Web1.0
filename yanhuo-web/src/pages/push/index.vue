<template>
  <div class="container" id="container">
    <div class="push-container" id="tagContainer">
      <div class="header">
        <span class="header-icon"></span><span class="header-title">发布图文</span>
      </div>
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
          <el-icon><Plus /></el-icon>
        </el-upload>

        <el-dialog v-model="dialogVisible">
          <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
      </div>
      <el-divider style="margin: 12px; width: 576px" />
      <div class="push-content">
        <el-input
          v-model="title"
          maxlength="20"
          show-word-limit
          type="text"
          placeholder="Please input"
          class="input-title"
        />
        <el-input
          :rows="5"
          maxlength="200"
          show-word-limit
          type="textarea"
          v-model="content"
          placeholder="Please input"
          class="input-content"
          id="inputContent"
        />

        <div
          v-infinite-scroll="loadMoreData"
          class="scroll-tag-container"
          v-show="showTagState"
        >
          <p
            v-for="(item, index) in selectTagList"
            :key="index"
            class="scrollbar-tag-item"
            @click="selectTag(item)"
          >
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
          <span class="btn-content"
            ><div class="smile"></div>
            表情</span
          >
        </button>
      </div>

      <div class="submit">
        <button class="publishBtn" @click="pubslish()">
          <span class="btn-content">发布</span>
        </button>
        <button class="clearBtn">
          <span class="btn-content">取消</span>
        </button>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { Plus } from "@element-plus/icons-vue";
import type { UploadUserFile, CascaderProps } from "element-plus";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/store/userStore";
import axios from "axios";
import { getCategoryTreeData } from "@/api/category";
import { saveNoteByDTO } from "@/api/note";
import { getPageTagByKeyword } from "@/api/tag";
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

const fileList = ref<UploadUserFile[]>([]);

const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const title = ref("");
const content = ref("");
const uploadHeader = ref({
  accessToken: userStore.getToken(),
});
const categoryList = ref<Array<any>>([]);
const options = ref([]);
const note = ref<any>({});
const showTagState = ref(false);
const tagList = ref<Array<any>>([]);
const selectTagList = ref<Array<any>>([]);
const currentPage = ref(1);
const pageSize = 10;
const tagTotal = ref(0);
import { baseURL } from "@/constant/constant";

// 监听外部点击
onMounted(() => {
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
});

const addTag = () => {
  content.value += "#";
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
  content.value += val.title;
  tagList.value.push(val.id);
  showTagState.value = false;
};

const loadMoreData = () => {
  currentPage.value += 1;
  setData();
};

const handleChange = (ids: Array<any>) => {
  categoryList.value = ids;
};

// 上传图片功能
const pubslish = () => {
  // 验证
  if (fileList.value.length <= 0) {
    ElMessage({
      message: "图片数量不能为空",
      type: "error",
    });
    return;
  }

  if (title.value === null || content.value == null) {
    ElMessage({
      message: "标题或内容不能为空",
      type: "error",
    });
    return;
  }

  if (categoryList.value.length <= 0) {
    ElMessage({
      message: "请选择分类",
      type: "error",
    });
    return;
  }

  const p = new Promise((resolve, reject) => {
    let params = new FormData();
    // 注意此处对文件数组进行了参数循环添加
    if (fileList.value.length > 0) {
      fileList.value.forEach((file: any) => {
        params.append("uploadFiles", file.raw);
      });
    } else {
      //that.$message.warning("当前没有合适图片可以上传");
    }
    axios({
      url: baseURL + "/util/oss/saveBatch/1",
      method: "post",
      data: params,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
      .then((res: any) => {
        resolve(res.data.data);
      })
      .catch((err: any) => {
        reject(err);
      });
  });

  p.then((data: any) => {
    note.value.urls = data;
    note.value.noteCover = data[0];
    note.value.count = data.length;
    note.value.type = 1;
    note.value.title = title.value;
    note.value.content = content.value.split("#")[0];
    note.value.cpid = categoryList.value[0];
    note.value.cid = categoryList.value[1];
    note.value.tagList = tagList.value;
    const coverImage = new Image();
    coverImage.src = data[0];
    coverImage.onload = () => {
      const size = coverImage.width / coverImage.height;
      note.value.noteCoverHeight = size >= 1.3 ? 200 : 300;
      saveNoteByDTO(note.value).then(() => {
        note.value = {};
        title.value = "";
        content.value = "";
        categoryList.value = [];
        fileList.value = [];
        tagList.value = [];
        ElMessage({
          message: "发布成功",
          type: "success",
        });
      });
    };
  });
};

const initData = () => {
  getCategoryTreeData().then((res) => {
    options.value = res.data;
  });
};

initData();
</script>
<style lang="less" scoped>
/deep/ .el-upload-list--picture-card .el-upload-list__item {
  width: 80px;
  height: 80px;
}
/deep/ .el-upload-list__item.is-success .el-upload-list__item-status-label {
  display: none;
}
/deep/ .el-upload--picture-card {
  width: 80px;
  height: 80px;
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
        font-family: RedNum, RedZh, RedEn, -apple-system;
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
