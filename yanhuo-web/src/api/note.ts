import request from "@/utils/request";

/**
 * 根据笔记id获取笔记
 * @param noteId 笔记id
 * @returns 笔记
 */
export const getNoteById = (noteId: string) => {
  return request<any>({
    url: "/platform/note/getNoteById", // mock接口
    method: "get",
    params: {
      noteId,
    },
  });
};

/**
 * 保存笔记
 * @param data 笔记实体
 * @returns 笔记id
 */
export const saveNoteByDTO = (data: any) => {
  return request<any>({
    url: "/platform/note/saveNoteByDTO", // mock接口
    method: "post",
    data: data,
    headers: { "Content-Type": "multipart/form-data;boundary=----WebKitFormBoundaryk4ZvuPo6pkphe7Pl" },
  });
};

/**
 * 更新笔记
 * @param data 笔记实体
 * @returns 笔记id
 */
export const updateNoteByDTO = (data: any) => {
  return request<any>({
    url: "/platform/note/updateNoteByDTO", // mock接口
    method: "post",
    data: data,
    headers: { "Content-Type": "multipart/form-data;boundary=----WebKitFormBoundaryk4ZvuPo6pkphe7Pl" },
  });
};

/**
 * 置顶笔记
 * @param noteId 
 * @returns 
 */
export const pinnedNote = (noteId: string) => {
  return request<any>({
    url: "/platform/note/pinnedNote", // mock接口
    method: "get",
    params: {
      noteId,
    },
  });
};

/**
 * 删除笔记
 * @param data 
 * @returns 
 */
export const deleteNoteByIds = (data: any) => {
  return request<any>({
    url: "/platform/note/deleteNoteByIds", // mock接口
    method: "post",
    data: data,
  });
};
