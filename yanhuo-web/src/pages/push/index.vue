<template>
  <div class="container">
    <div class="push-container">
      <div class="header">
        <span class="header-icon"></span><span class="header-title">发布图文</span>
      </div>
      <div class="img-list">
        <el-upload
          v-model:file-list="fileList"
          action="http://localhost:88/api/util/oss/saveBatch/0"
          list-type="picture-card"
          multiple
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          :limit="9"
          :on-exceed="handleExceed"
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
          placeholder="Please input"
          class="input-content"
        />
      </div>
      <div class="btns">
        <button class="css-fm44j css-osq2ks dyn">
          <span class="btn-content"># 话题</span></button
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
import { ref } from "vue";
import { Plus } from "@element-plus/icons-vue";

import type { UploadProps, UploadUserFile, UploadInstance } from "element-plus";
import { ElMessage, ElMessageBox } from "element-plus";
import { useUserStore } from "@/store/userStore";
import axios from "axios";

const userStore = useUserStore();

const fileList = ref<UploadUserFile[]>([
  // {
  //   name: "food.jpeg",
  //   url:
  //     "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100",
  // },
]);

const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const title = ref("");
const uploadHeader = ref({
  accessToken: userStore.getToken(),
});

const handleRemove: UploadProps["onRemove"] = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles);
};

const handlePreview: UploadProps["onPreview"] = (uploadFile) => {
  console.log(uploadFile);
};

const handleExceed: UploadProps["onExceed"] = (files, uploadFiles) => {
  ElMessage.warning(
    `The limit is 3, you selected ${files.length} files this time, add up to ${
      files.length + uploadFiles.length
    } totally`
  );
};

const beforeRemove: UploadProps["beforeRemove"] = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(`Cancel the transfer of ${uploadFile.name} ?`).then(
    () => true,
    () => false
  );
};

// 上传图片功能
const pubslish = () => {
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
    url: "http://localhost:88/api/util/oss/saveBatch/0",
    method: "post",
    data: params,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
    .then((res: any) => {
      console.log("上传成功", res.data);
    })
    .catch((err: any) => {
      console.log("上传成功", err.data);
    });
};
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
  padding: 0 24px;
  padding-top: 72px;
  width: 67%;
  margin: 0 auto;
  .push-container {
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
