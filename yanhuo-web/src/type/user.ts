export interface UserLogin {
  phone: string;
  email: string;
  code: string;
}

export interface User {
  value: any;
  id: string;
  yxId: string;
  username: string;
  avatar: string;
  gender: number;
  description: string;
  birthday: string;
  address: string;
  userCover: string;
  trendCount: number;
  followerCount: number;
  fanCount: number;
}
