import request from "@/utils/request";

export const postData = (data: any) => {
  return request<any>({
    url: "/auth/auth/login", // mock接口
    method: "post",
    data,
  });
};

export const getUserInfoByToken = (accessToken: string) => {
  return request<any>({
    url: "/auth/auth/getUserInfoByToken", // mock接口
    method: "get",
    params: {
      accessToken: accessToken,
    },
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

export const refreshToken = (refreshToken: string) => {
  return request<any>({
    url: `/auth/auth/refreshToken/`, // mock接口
    method: "get",
    params: {
      refreshToken: refreshToken,
    },
  });
};
