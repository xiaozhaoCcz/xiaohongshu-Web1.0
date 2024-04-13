import axios, { InternalAxiosRequestConfig, AxiosResponse } from "axios";
import { useUserStore } from "@/store/userStore";
import { storage } from "./storage";
import { ElMessage } from "element-plus";
import { baseURL } from "@/constant/constant";
// 刷新 token 后, 将缓存的接口重新请求一次
// 是否正在刷新 token
let isRefreshing: boolean = false;

// 存储待重发的请求
let requestsQueue: ((token: string) => any)[] = [];

// 创建 axios 实例
const service = axios.create({
  // baseURL: import.meta.env.VITE_APP_BASE_API,
  baseURL: baseURL,
  timeout: 50000,
  headers: { "Content-Type": "application/json;charset=utf-8" },
});

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const userStore = useUserStore();
    if (userStore.getToken()) {
      config.headers.accessToken = userStore.getToken();
      config.headers.userId = userStore.getUserInfo().id;
    }
    return config;
  },
  (error: any) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code } = response.data;
    console.log(response, code);
    if (code === 200) {
      return response.data;
    }
    // 响应数据为二进制流处理(Excel导出)
    // if (response.data instanceof ArrayBuffer) {
    //   return response;
    // }
    const config = response.config;

    const userStore = useUserStore();
    if (code === 501) {
      // 无感刷新Token
      if (!isRefreshing) {
        isRefreshing = true;
        storage.set("accessToken", "");
        const refreshToken = storage.get("refreshToken") as string;
        console.log("refreshToken", refreshToken);
        return userStore
          .getNewToken(refreshToken)
          .then(async (rftRes) => {
            console.log("rftRes", rftRes);
            const { accessToken, refreshToken } = rftRes.data;
            storage.set("accessToken", accessToken);
            storage.set("refreshToken", refreshToken);
            config.headers.accessToken = accessToken;
            // 重新请求一下第一个 501 的接口
            const firstReqRes = await service.request(config);
            // token 刷新后将数组的方法重新执行
            requestsQueue.forEach((cb: any) => cb(accessToken));
            // 队列中的请求执行完毕后，清空数组
            requestsQueue = [];
            return firstReqRes;
          }).finally(() => {
            isRefreshing = false;
          });
      } else {
        // 如果正在 refreshToken

        // 如果有多个请求同时发起，第一个请求 401 了，refreshToken 又正在进行中
        // 那么把第一个以外的请求暂存起来
        return new Promise((resolve) => {
          // 用函数形式将 resolve 存入，等待 refreshToken 完成后再执行
          requestsQueue.push((token: string) => {
            config.headers.Authorization = token;
            resolve(service.request(config));
          });
        });
      }
    } else if (code == 401) {
      ElMessage.error("登录过期，请重新登陆");
      window.localStorage.clear();
      setTimeout(() => {
        window.location.href = "/"
      }, 500);
    }
    return Promise.reject(response.data);
  }
);

// 导出 axios 实例
export default service;
