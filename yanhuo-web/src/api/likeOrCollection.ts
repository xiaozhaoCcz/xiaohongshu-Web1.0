import request from "@/utils/request";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";

export const likeOrCollectionByDTO = (data: LikeOrCollectionDTO) => {
  return request<any>({
    url: `/platform/likeOrCollection/likeOrCollectionByDTO`, // mock接口
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
