import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";

const app = createApp(App);

import router from "./router/index";

import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

// 引入动画
import 'animate.css';
import "@/assets/font_4394635_lwuldvb474/iconfont.css";

app.use(router);
app.use(ElementPlus);
app.mount("#app");

