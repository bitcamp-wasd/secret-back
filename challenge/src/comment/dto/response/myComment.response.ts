import { Types } from 'mongoose';

export class MyCommentResDto {
  totalPages: number;
  content: any[];

  constructor(pageComments: any) {
    this.totalPages = pageComments.totalPages;
    this.content = pageComments.docs.map(
      (element) =>
        new Content(
          element._id,
          element.comment,
          element.videoId.title,
          element.createdAt,
          element.videoId._id,
        ),
    );
  }
}

class Content {
  constructor(commentId, comment, title, createDate, videoId) {
    this.commentId = commentId;
    this.comment = comment;
    this.title = title;
    this.createDate = createDate;
    this.videoId = videoId;
  }

  commentId: Types.ObjectId;
  comment: string;
  title: string;
  createDate: Date;
  videoId: Types.ObjectId;
}
