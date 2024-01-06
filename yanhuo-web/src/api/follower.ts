import request from "@/utils/request";

export const getFollowTrendPage = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/platform/follower/getFollowTrendPage/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};
