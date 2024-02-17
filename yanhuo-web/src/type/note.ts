export interface NoteSearch {
  id: string;
  title: string;
  src: string;
  uid: string;
  username: string;
  avatar: string;
  cid: string;
  cpid: string;
  urls: string;
  count: number;
  type: number;
  likeCount: number;
  time: number | string;
}

export interface NoteInfo {
  id: string;
  title: string;
  content: string;
  noteCover: string;
  uid: string;
  username: string;
  avatar: string;
  imgList: Array<string>;
  type: number;
  likeCount: number;
  collectionCount: number;
  commentCount: number;
  tagList: Array<any>;
  time: string;
  isFollow: boolean,
  isLike: boolean,
  isCollection: boolean,
  pinned: number
}

export interface NoteDTO {
  keyword: string;
  type: number;
  cid: string;
  cpid: string;
}
