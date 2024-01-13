import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";

const app = createApp(App);

import router from "./router/index";

import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

// 引入动画
import 'animate.css';

import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

import Prism from 'prismjs';


VueMarkdownEditor.use(vuepressTheme, {
    Prism,
  });
  

import "@/assets/font_4394635_lwuldvb474/iconfont.css";

app.use(router);
app.use(ElementPlus);
app.use(VueMarkdownEditor);
app.mount("#app");

