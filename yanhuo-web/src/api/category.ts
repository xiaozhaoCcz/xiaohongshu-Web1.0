import request from "@/utils/request";
/**
 * 获取树形分类数据
 * @returns 分类数据
 */
export const getCategoryTreeData = () => {
  return request<any>({
    url: "/platform/category/getCategoryTreeData",
    method: "get",
  });
};
