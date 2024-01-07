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

export const saveNoteByDTO = (data:any) => {
  return request<any>({
    url: "/platform/note/saveNoteByDTO", // mock接口
    method: "post",
    data:data
  });
};

