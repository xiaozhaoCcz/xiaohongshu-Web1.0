import request from "@/utils/request";

export const getCountMessage = () => {
  return request<any>({
    url: "/im/chat/getCountMessage", // mock接口
    method: "get",
  });
};

export const getChatUserList = () => {
  return request<any>({
    url: "/im/chat/getChatUserList", // mock接口
    method: "get",
  });
};

export const getAllChatRecord = (
  currentPage: number,
  pageSize: number,
  acceptUid: string
) => {
  return request<any>({
    url: `/im/chat/getAllChatRecord/${currentPage}/${pageSize}`, // mock接口
    method: "get",
    params: {
      acceptUid,
    },
  });
};

export const sendMsg = (data: any) => {
  return request<any>({
    url: "/im/chat/sendMsg", // mock接口
    method: "post",
    data: data,
  });
};
