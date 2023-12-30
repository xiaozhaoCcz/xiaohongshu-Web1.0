import { defineStore } from "pinia";
import { ref } from "vue";
import { storage } from "@/utils/storage";
import { postData, refreshToken } from "@/api/user";
import { store } from "@/store";

interface LoginData {
  username: string;
  password: string;
}

// 使用setup模式定义
export const userStore = defineStore("userStore", () => {
  const token = ref("");

  const getToken = () => {
    return storage.get("accessToken");
  };

  const login = (loginData: LoginData) => {
    return new Promise<void>((resolve, reject) => {
      postData(loginData)
        .then((res) => {
          storage.set("accessToken", res.data.accessToken);
          storage.set("refreshToken", res.data.refreshToken);
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  };

  const getNewToken = (token: string) => {
    return new Promise<any>((resolve, reject) => {
      refreshToken(token)
        .then((res) => {
          console.log(111, res.data);
          resolve(res);
        })
        .catch((error) => {
          reject(error);
        });
    });
  };

  return { token, getToken, login, getNewToken };
});

export function useUserStore() {
  return userStore(store);
}
