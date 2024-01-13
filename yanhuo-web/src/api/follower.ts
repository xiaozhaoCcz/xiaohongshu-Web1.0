import request from "@/utils/request";

export const getFollowTrendPage = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/platform/follower/getFollowTrendPage/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};

export const followById = (followerId: string) => {
  return request<any>({
    url: `/platform/follower/followById`, // mock接口
    method: "get",
    params: {
      followerId,
    },
  });
};

export const isFollow = (followerId: string) => {
  return request<any>({
    url: `/platform/follower/isFollow`, // mock接口
    method: "get",
    params: {
      followerId,
    },
  });
};

export const getNoticeFollower = (currentPage: number, pageSize: number) => {
  return request<any>({
    url: `/platform/follower/getNoticeFollower/${currentPage}/${pageSize}`, // mock接口
    method: "get",
  });
};
