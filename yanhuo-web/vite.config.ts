import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
import { resolve } from "path";
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": resolve(__dirname, "./src"),
    },
    //extensions: [".ts", ".js", ".vue", ".json", ".mjs"],
    extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".vue"],
  },
  server: {
    // port: 3000,
    open: true,
    // 配置代理
    proxy: {
      // 请求的路径前缀只要是 /testaxios 就会被拦截走这个代理
      '/oss': {
      /**
        *  请求的目标资源再经过替换成 /httphwm/getList 后，
        *  会加上 http://127.0.0.1:9693 这个前缀，
        *  最后请求的URL为: http://127.0.0.1:9693/httphwm/getList
        */
        target: 'http://localhost:88/api/platform/',
        ws: true,
        changeOrigin: true,
        // 拦截到的请求路径 testaxios/httphwm/getList，/testaxios会被替换成空
        rewrite: (path) => path.replace(/^\/oss/, 'oss'),
      },
    },
  }
});
