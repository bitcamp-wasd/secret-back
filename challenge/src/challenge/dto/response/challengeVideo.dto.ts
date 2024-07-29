import { Types } from 'mongoose';

export class ChallengeVideo {
  videoId: Types.ObjectId;
  videoPath: string;
  title: string;
  category: string;
  thumbnailPath: string;
  description: string;
  length: number;
  userId: number;
  nickname: string;
  rankname: string;
  cnt: number;

  constructor(
    _id: Types.ObjectId,
    videoPath: string,
    title: string,
    category: string,
    thumbnailPath: string,
    description: string,
    length: number,
    userId: number,
    cnt: number,
    user,
  ) {
    this.videoId = _id;
    this.videoPath = videoPath;
    this.title = title;
    this.category = category;
    this.thumbnailPath = thumbnailPath;
    this.description = description;
    this.length = length;
    this.userId = userId;
    this.nickname = user.nickname;
    this.rankname = user.rankname;
    this.cnt = cnt;
  }
}
