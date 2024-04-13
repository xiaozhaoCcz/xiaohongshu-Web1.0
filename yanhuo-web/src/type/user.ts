export interface UserLogin {
  phone: string;
  email: string;
  code: string;
  username: string;
  password: string;
}

export interface User {
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
