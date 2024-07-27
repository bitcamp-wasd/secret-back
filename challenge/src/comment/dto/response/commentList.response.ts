import { Types } from 'mongoose';

export class CommentListResDto {
  constructor(
    commnetId: Types.ObjectId,
    comment: string,
    videoId: Types.ObjectId,
    createdAt: Date,
    userId: number,
    user: any,
  ) {
    this.commentId = commnetId;
    this.comment = comment;
    this.videoId = videoId;
    this.createdAt = createdAt;
    this.userId = userId;
    this.nickname = user.nickname;
    this.rankname = user.rankname;
  }

  commentId: Types.ObjectId;
  comment: string;
  videoId: Types.ObjectId;
  createdAt: Date;
  userId: number;
  nickname: string;
  rankname: string;
}
