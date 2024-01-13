import request from "@/utils/request";

export const getPageTagByKeyword = (currentPage: number, pageSize: number,keyword:string) => {
    return request<any>({
      url: `/platform/tag/getPageTagByKeyword/${currentPage}/${pageSize}`, // mock接口
      method: "get",
      params:{
        keyword
      }
    });
  };