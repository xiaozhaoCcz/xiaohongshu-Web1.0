import { defineStore } from "pinia";
import { ref } from "vue";
import { storage } from "@/utils/storage";
import { refreshToken } from "@/api/user";
import { store } from "@/store";

// 使用setup模式定义
export const userStore = defineStore("userStore", () => {
  const token = ref("");

  const getToken = () => {
    return storage.get("accessToken");
  };

  const getUserInfo = () => {
    return storage.get("userInfo");
  };

  const setUserInfo = (data: any) => {
    storage.set("userInfo", data);
  };

  const getNewToken = (token: string) => {
    return new Promise<any>((resolve, reject) => {
      refreshToken(token)
        .then((res) => {
          resolve(res);
        })
        .catch((error) => {
          reject(error);
        });
    });
  };

  return { token, getToken, getNewToken, getUserInfo, setUserInfo };
});

export function useUserStore() {
  return userStore(store);
}
