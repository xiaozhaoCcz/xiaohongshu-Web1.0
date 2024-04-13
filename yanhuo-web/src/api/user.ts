import request from "@/utils/request";
import type { UserLogin } from "@/type/user";

/**
 * 
 * @param data 
 * @returns 
 */
export const login = (data: any) => {
  return request<any>({
    url: "/auth/auth/login", // mock接口
    method: "post",
    data,
  });
};

/**
 * 
 * @param deptId 
 * @param file 
 * @returns 
 */
export function importFile(deptId: number, file: File) {
  const formData = new FormData();
  formData.append("file", file);
  return request({
    url: "/api/v1/users/_import",
    method: "post",
    params: { deptId: deptId },
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

/**
 * 
 * @param accessToken 
 * @returns 
 */
export const getUserInfoByToken = (accessToken: string) => {
  return request<any>({
    url: "/auth/auth/getUserInfoByToken", // mock接口
    method: "get",
    params: {
      accessToken,
    },
  });
};

/**
 * 
 * @param refreshToken 
 * @returns 
 */
export const refreshToken = (refreshToken: string) => {
  return request<any>({
    url: `/auth/auth/refreshToken/`, // mock接口
    method: "get",
    params: {
      refreshToken,
    },
  });
};

/**
 * 
 * @param data 
 * @returns 
 */
export const loginByCode = (data: UserLogin) => {
  return request<any>({
    url: "/auth/auth/loginByCode", // mock接口
    method: "post",
    data,
  });
};

/**
 * 
 * @param currentPage 
 * @param pageSize 
 * @param userId 
 * @param type 
 * @returns 
 */
export const getTrendPageByUser = (currentPage:number,pageSize:number,userId:string,type:number) => {
  return request<any>({
    url: `/platform/user/getTrendPageByUser/${currentPage}/${pageSize}`, // mock接口
    method: "get",
    params: {
      userId,
      type
    },
  });
};

/**
 * 
 * @param userId 
 * @returns 
 */
export const getUserById = (userId:string) => {
  return request<any>({
    url: `/platform/user/getUserById`, // mock接口
    method: "get",
    params: {
      userId
    },
  });
};

/**
 * 
 * @param userId 
 * @returns 
 */
export const loginOut = (userId:string) => {
  return request<any>({
    url: `/auth/auth/loginOut`, // mock接口
    method: "get",
    params: {
      userId
    },
  });
};


