import request from "@/utils/request";

export const getRecommendNotePage = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/search/note/getRecommendNotePage/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};
