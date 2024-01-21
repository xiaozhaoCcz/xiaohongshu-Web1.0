import loading from "@/assets/loading.png";
import error from "@/assets/error.png";
import { reactive } from "vue";

export const wsKey = "ws://127.0.0.1:8802/im/ws/";

// 图片必须保持在336和186之间

export const options = reactive({
    // 唯一key值
    rowKey: "id",
    // 卡片之间的间隙
    gutter: 10,
    // 是否有周围的gutter
    hasAroundGutter: false,
    // 卡片在PC上的宽度
    width: 240,
    // 自定义行显示个数，主要用于对移动端的适配
    breakpoints: {
      1200: {
        // 当屏幕宽度小于等于1200
        rowPerView: 4,
      },
      // 800: {
      //   // 当屏幕宽度小于等于800
      //   rowPerView: 3,
      // },
      500: {
        // 当屏幕宽度小于等于500
        rowPerView: 2,
      },
    },
    // 动画效果
    animationEffect: "animate__zoomIn",
    // 动画时间
    animationDuration: 2000,
    // 动画延迟
    animationDelay: 300,
    // 背景色
    backgroundColor: "#2C2E3A",
    // imgSelector
    imgSelector: "src.original",
    // 加载配置
    loadProps: {
      loading,
      error,
    },
    // 是否懒加载
    lazyload: true,
  });