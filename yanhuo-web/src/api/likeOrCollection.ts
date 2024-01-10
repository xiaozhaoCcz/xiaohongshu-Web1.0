import request from "@/utils/request";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";

export const likeByDTO = (data: LikeOrCollectionDTO) => {
  return request<any>({
    url: `/platform/likeOrCollection/likeByDTO`, // mock接口
    method: "post",
    data: data,
  });
};

export const isLikeOrCollection = (data: LikeOrCollectionDTO) => {
  return request<any>({
    url: `/platform/likeOrCollection/isLikeOrCollection`, // mock接口
    method: "post",
    data: data,
  });
};
