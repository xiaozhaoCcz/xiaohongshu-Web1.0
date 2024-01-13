import { defineStore } from "pinia";
import { ref } from "vue";
import { storage } from "@/utils/storage";
import { store } from "@/store";

// 使用setup模式定义
export const searchStore = defineStore("searchStore", () => {
  const keyWord = ref("");
  const seed = ref("");

  const setKeyword = (val: string) => {
    keyWord.value = val;
    storage.set("keyword", val);
  };


  const setSeed = (val:string) =>{
    seed.value = val;
    storage.set("seed", val);
  }

  const pushRecord = (val: string) => {
    if (storage.get("historyRecords") == null) {
      storage.set("historyRecords", []);
    }
    let data = storage.get("historyRecords") as Array<string>;
    data = data.filter((item) => item != val);
    data.splice(0, 0, val);
    if (data.length > 10) {
      data = data.slice(0, data.length - 1);
    }
    storage.set("historyRecords", data);
  };

  const getRecords = () => {
    return storage.get("historyRecords") as Array<string>;
  };

  const clearAllRecord = () => {
    storage.set("historyRecords", []);
  };

  const deleteRecord = (index: number) => {
    const data = storage.get("historyRecords") as Array<string>;
    data.splice(index, 1);
  };

  return { keyWord,seed, setKeyword, pushRecord, deleteRecord, clearAllRecord, getRecords,setSeed };
});

export function useSearchStore() {
  return searchStore(store);
}
