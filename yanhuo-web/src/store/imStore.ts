import { defineStore } from "pinia";
import { ref } from "vue";
import { store } from "@/store";

// 使用setup模式定义
export const imStore = defineStore("imStore", () => {
  const userList = ref<Array<any>>([]);

  const message = ref<any>({});

  const countMessage = ref({
    chatCount: 0,
    likeOrCollectionCount: 0,
    commentCount: 0,
    followCount: 0,
  });


  const setUserList = (data: Array<any>) => {
    userList.value = data;
  };

  const setCountMessage = (data: any) => {
    countMessage.value = data;
  };

  const setMessage = (data: any) => {
    message.value = data;
  };

  return { userList, countMessage, message, setUserList, setCountMessage, setMessage };
});

export function useImStore() {
  return imStore(store);
}
