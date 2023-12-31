import request from "@/utils/request";

export const getNoteById = (noteId: string) => {
  return request<any>({
    url: "/platform/note/getNoteById", // mock接口
    method: "get",
    params: {
      noteId: noteId,
    },
  });
};
