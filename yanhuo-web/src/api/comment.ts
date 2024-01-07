import request from "@/utils/request";
import type { CommentDTO } from "@/type/comment";

export const getCommentPageWithCommentByNoteId = (
  currentPage: number,
  pageSize: number,
  noteId: string
) => {
  return request<any>({
    url: `/platform/comment/getCommentPageWithCommentByNoteId/${currentPage}/${pageSize}`, // mock接口
    method: "get",
    params: {
      noteId,
    },
  });
};

export const saveCommentByDTO = (data: CommentDTO) => {
  return request<any>({
    url: `/platform/comment/saveCommentByDTO`, // mock接口
    method: "post",
    data: data,
  });
};

export const syncCommentByIds = (data: Array<string>) => {
  return request<any>({
    url: `/platform/comment/syncCommentByIds`, // mock接口
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
    url: `/platform/comment/getTwoCommentPageByOneCommentId/${currentPage}/${pageSize}`, // mock接口
    method: "get",
    params: {
      oneCommentId,
    },
  });
};