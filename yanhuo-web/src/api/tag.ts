import request from "@/utils/request";

/**
 * 
 * @param currentPage 
 * @param pageSize 
 * @param keyword 
 * @returns 
 */
export const getPageTagByKeyword = (currentPage: number, pageSize: number,keyword:string) => {
    return request<any>({
      url: `/platform/tag/getPageTagByKeyword/${currentPage}/${pageSize}`, // mock接口
      method: "get",
      params:{
        keyword
      }
    });
  };