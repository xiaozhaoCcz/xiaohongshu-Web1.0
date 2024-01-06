import request from "@/utils/request";
import {NoteDTO} from "@/type/note"

export const getRecommendNotePage = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/search/note/getRecommendNotePage/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};

export const getNotePageByDTO = (currentPage: number, pageSize: number,data:NoteDTO) => {
  return request<any>({
    url: `/search/note/getNotePageByDTO/${currentPage}/${pageSize}`, // mock接口
    method: "post",
    data: data
  });
};
