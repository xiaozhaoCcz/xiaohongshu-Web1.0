import request from "@/utils/request";

export const getCategoryTreeData = () => {
  return request<any>({
    url: "/platform/category/getCategoryTreeData",
    method: "get",
  });
};
