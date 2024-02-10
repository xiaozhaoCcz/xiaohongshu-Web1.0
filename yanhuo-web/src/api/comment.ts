import request from "@/utils/request";
import type { CommentDTO } from "@/type/comment";

/**
 * 得到所有的一级评论并携带二级评论
 * @param currentPage 当前页
 * @param pageSize 分页数
 * @param noteId 笔记id
 * @returns 评论结果集
 */
export const getCommentPageWithCommentByNoteId = (
  currentPage: number,
  pageSize: number,
  noteId: string
) => {
  return request<any>({
    url: `/platform/comment/getCommentPageWithCommentByNoteId/${currentPage}/${pageSize}`,
    method: "get",
    params: {
      noteId,
    },
  });
};

/**
 * 保存评论
 * @param data 评论实体
 * @returns 增加后的评论实体
 */
export const saveCommentByDTO = (data: CommentDTO) => {
  return request<any>({
    url: `/platform/comment/saveCommentByDTO`, 
    method: "post",
    data: data,
  });
};

/**
 * 根据评论id同步评论集
 * @param data 评论id数据集
 * @returns 
 */
export const syncCommentByIds = (data: Array<string>) => {
  return request<any>({
    url: `/platform/comment/syncCommentByIds`,
    method: "post",
    data: data,
  });
};

/**
 * 根据一级评论id获取所有的二级评论
 * @param currentPage 当前页
 * @param pageSize 分页数
 * @param oneCommentId 一级评论id
 * @returns 评论结果集
 */
export const getTwoCommentPageByOneCommentId = (
  currentPage: number,
  pageSize: number,
  oneCommentId: string
) => {
  return request<any>({
    url: `/platform/comment/getTwoCommentPageByOneCommentId/${currentPage}/${pageSize}`,
    method: "get",
    params: {
      oneCommentId,
    },
  });
};

/**
 * 获取当前用户通知的评论集
 * @param currentPage 当前页
 * @param pageSize 分页数
 * @returns 评论结果集
 */
export const getNoticeComment = (
  currentPage: number,
  pageSize: number,
) => {
  return request<any>({
    url: `/platform/comment/getNoticeComment/${currentPage}/${pageSize}`,
    method: "get",
  });
};