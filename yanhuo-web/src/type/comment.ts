export interface CommentDTO {
  nid: string;
  noteUid: string;
  pid: string;
  replyId: string;
  replyUid: string;
  replyUsername: string;
  level: number;
  content: string;
}

export interface Comment {
  id: string;
  pid: string;
  nid: string;
  noteCover: string;
  uid: string;
  username: string;
  avatar: string;
  replyId: string;
  replyUid: string;
  replyUsername: string;
  content: string;
  replyContent: string;
  time: number;
  likeCount: number;
  isLike: boolean;
  twoCommentCount: number;
  children: Array<Comment>;
}
