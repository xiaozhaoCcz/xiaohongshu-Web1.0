import request from "@/utils/request";
import type { LikeOrCollectionDTO } from "@/type/likeOrCollection";
/**
 * 点赞或收藏
 * @param data 点赞收藏实体
 * @returns success
 */
export const likeOrCollectionByDTO = (data: LikeOrCollectionDTO) => {
  return request<any>({
    url: `/platform/likeOrCollection/likeOrCollectionByDTO`, // mock接口
    method: "post",
    data: data,
  });
};

/**
 * 是否点赞或收藏
 * @param data 点赞收藏实体
 * @returns 
 */
export const isLikeOrCollection = (data: LikeOrCollectionDTO) => {
  return request<any>({
    url: `/platform/likeOrCollection/isLikeOrCollection`, // mock接口
    method: "post",
    data: data,
  });
};

/**
 * 得到当前用户最新的点赞和收藏信息
 * @param currentPage 当前页
 * @param pageSize 分页数
 * @returns page
 */
export const getNoticeLikeOrCollection = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/platform/likeOrCollection/getNoticeLikeOrCollection/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};


