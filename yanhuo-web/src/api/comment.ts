import request from "@/utils/request";
import type { CommentDTO } from "@/type/comment";

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

export const saveCommentByDTO = (data: CommentDTO) => {
  return request<any>({
    url: `/platform/comment/saveCommentByDTO`, 
    method: "post",
    data: data,
  });
};

export const syncCommentByIds = (data: Array<string>) => {
  return request<any>({
    url: `/platform/comment/syncCommentByIds`,
    method: "post",
    data: data,
  });
};


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

export const getNoticeComment = (
  currentPage: number,
  pageSize: number,
) => {
  return request<any>({
    url: `/platform/comment/getNoticeComment/${currentPage}/${pageSize}`,
    method: "get",
  });
};