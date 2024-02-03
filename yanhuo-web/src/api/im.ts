import request from "@/utils/request";

/**
 * 得到所有聊天的记录数量
 * @returns 聊天数量
 */
export const getCountMessage = () => {
  return request<any>({
    url: "/im/chat/getCountMessage", // mock接口
    method: "get",
  });
};

/**
 * 获取当前用户下所有聊天的用户信息
 * @returns 聊天的用户信息
 */
export const getChatUserList = () => {
  return request<any>({
    url: "/im/chat/getChatUserList", // mock接口
    method: "get",
  });
};

/**
 * 清除聊天数量
 * @param sendUid 发送方的用户id
 * @param type 类型
 * @returns success
 */
export const clearMessageCount = (sendUid:string,type:number) => {
  return request<any>({
    url: "/im/chat/clearMessageCount", // mock接口
    method: "get",
    params:{
      sendUid,
      type
    }
  });
};

/**
 * 获取所有的聊天记录
 * @param currentPage 分页
 * @param pageSize 分页数
 * @param acceptUid 接收方的用户id
 * @returns 聊天记录
 */
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

/**
 * 发送消息
 * @param data 消息实体
 * @returns success
 */
export const sendMsg = (data: any) => {
  return request<any>({
    url: "/im/chat/sendMsg", // mock接口
    method: "post",
    data: data,
  });
};
