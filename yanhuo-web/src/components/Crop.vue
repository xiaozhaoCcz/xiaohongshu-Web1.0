<template>
  <!-- 选择图片 -->
  <section class="section">
    <div class="desc">
      <p>在 Vue 3.0 ，优先推荐使用 Composition API 来写组件</p>

      <a
        href="https://github.com/chengpeiquan/vue-picture-cropper-demo/blob/main/src/views/composition.vue"
        target="_blank"
      >
        <span>查看本 DEMO 源码</span>
      </a>
    </div>

    <p>请先点击按钮选择图片，会进入裁切处理环节</p>

    <!-- 设置一个按钮 -->
    <Button class="select-picture">
      <template #icon><UploadOutlined /></template>
      <span>选择图片</span>
      <input
        ref="uploadInput"
        type="file"
        accept="image/jpg, image/jpeg, image/png, image/gif"
        @change="selectFile"
      />
    </Button>

    <Button @click="isShowDialog = false">取消</Button>
    <Button @click="clear">清除</Button>
    <Button @click="reset">重置</Button>
    <Button type="primary" @click="getResult">裁切</Button>
    <!-- 设置一个按钮 -->
  </section>
  <!-- 选择图片 -->

  <!-- 结果预览区 -->
  <section class="section" v-if="result.dataURL && result.blobURL">
    <p>裁切后的 Base64 图片预览：</p>
    <div class="preview">
      <img :src="result.dataURL" alt="组合式 API" />
    </div>
    <p>裁切后的 Blob 图片预览：</p>
    <div class="preview">
      <img :src="result.blobURL" alt="组合式 API" />
    </div>
    <p>可以按 F12 查看打印的 base64 / blob / file 结果</p>
  </section>

  <!-- 结果预览区 -->

  <!-- 图片裁切插件 -->
  <VuePictureCropper
    :boxStyle="{
      width: '240px',
      height: '400px',
      backgroundColor: '#f8f8f8',
      margin: 'auto',
    }"
    :img="pic"
    :options="{
      viewMode: 1,
      dragMode: 'move',
      aspectRatio: 1,
      cropBoxResizable: false,
    }"
    :presetMode="{
      mode: 'fixedSize',
      width: 240,
      height: 330,
    }"
  />
  <!-- 图片裁切插件 -->
  <!-- 用于裁切的弹窗 -->
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import VuePictureCropper, { cropper } from "vue-picture-cropper";

interface Result {
  dataURL: string;
  blobURL: string;
}

export default defineComponent({
  components: {
    VuePictureCropper,
  },
  setup() {
    const uploadInput = ref<HTMLInputElement | null>(null);
    const pic = ref<string>("");
    const result: Result = reactive({
      dataURL: "",
      blobURL: "",
    });
    const isShowDialog = ref<boolean>(false);

    /**
     * 选择图片
     */
    const selectFile = (e: Event): void => {
      // 重置上一次的结果
      result.dataURL = "";
      result.blobURL = "";

      // 如果有多个裁剪框，也需要重置掉裁剪目标的值，避免使用同一张图片无法触发watch
      pic.value = "";

      // 获取选取的文件
      const target = e.target as HTMLInputElement;
      const { files } = target;
      if (!files) return;
      const file: File = files[0];

      // 转换为base64传给裁切组件
      const reader: FileReader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (): void => {
        // 更新裁切弹窗的图片源
        pic.value = String(reader.result);

        // 显示裁切弹窗
        isShowDialog.value = true;

        // 清空已选择的文件
        if (!uploadInput.value) return;
        uploadInput.value.value = "";
      };
    };

    /**
     * 获取裁切结果
     */
    const getResult = async (): Promise<void> => {
      // console.log(cropper)

      // 获取生成的base64图片地址
      const base64: string = cropper.getDataURL();

      // 获取生成的blob文件信息
      const blob: Blob = await cropper.getBlob();

      // 获取生成的file文件信息
      const file = await cropper.getFile({
        fileName: "测试文件名，可不传",
      });

      console.log({ base64, blob, file });

      // 把base64赋给结果展示区
      result.dataURL = base64;
      try {
        result.blobURL = URL.createObjectURL(blob);
      } catch (e) {
        result.blobURL = "";
      }

      // 隐藏裁切弹窗
      isShowDialog.value = false;
    };

    /**
     * 清除裁切框
     */
    const clear = (): void => {
      cropper.clear();
    };

    /**
     * 重置默认的裁切区域
     */
    const reset = (): void => {
      cropper.reset();
    };

    return {
      // 数据
      uploadInput,
      pic,
      result,
      isShowDialog,

      // 方法
      selectFile,
      getResult,
      clear,
      reset,
    };
  },
});
</script>

<style scoped></style>
