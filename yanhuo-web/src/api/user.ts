import request from "@/utils/request";
import type { UserLogin } from "@/type/user";

export const login = (data: any) => {
  return request<any>({
    url: "/auth/auth/login", // mock接口
    method: "post",
    data,
  });
};

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

export const getUserInfoByToken = (accessToken: string) => {
  return request<any>({
    url: "/auth/auth/getUserInfoByToken", // mock接口
    method: "get",
    params: {
      accessToken,
    },
  });
};

export const refreshToken = (refreshToken: string) => {
  return request<any>({
    url: `/auth/auth/refreshToken/`, // mock接口
    method: "get",
    params: {
      refreshToken,
    },
  });
};

export const loginByCode = (data: UserLogin) => {
  return request<any>({
    url: "/auth/auth/loginByCode", // mock接口
    method: "post",
    data,
  });
};

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

export const getUserById = (userId:string) => {
  return request<any>({
    url: `/platform/user/getUserById`, // mock接口
    method: "get",
    params: {
      userId
    },
  });
};

export const loginOut = (userId:string) => {
  return request<any>({
    url: `/auth/auth/loginOut`, // mock接口
    method: "get",
    params: {
      userId
    },
  });
};


