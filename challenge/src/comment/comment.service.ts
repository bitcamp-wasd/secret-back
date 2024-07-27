import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { CreateCommentReqDto } from './dto/request/createComment.request';
import { InjectModel } from '@nestjs/mongoose';
import mongoose, { Model, PaginateModel } from 'mongoose';
import { Comment } from './comment.collection';
import { RestApiService } from 'src/restApi/api.service';
import { CommentListResDto } from './dto/response/commentList.response';
import { PatchCommentReqDto } from './dto/request/patchComment.request';
import { DeleteCommentReqDto } from './dto/request/deleteComment.request';
import { MyCommentResDto } from './dto/response/myComment.response';

@Injectable()
export class CommentService {
  constructor(
    @InjectModel(Comment.name) private readonly commentModel: Model<Comment>,
    @InjectModel(Comment.name) private commentPageModel: PaginateModel<Comment>,
    private readonly restApiService: RestApiService,
  ) {}

  async myComment(userAuth: UserAuthDto, pageNumber: number) {
    const comments: any = await this.commentPageModel.paginate(
      { userId: userAuth.userId },
      {
        sort: { createdAt: -1 },
        limit: 10,
        page: pageNumber + 1,
        populate: 'videoId',
      },
    );

    const commentsDto = new MyCommentResDto(comments);

    return commentsDto;
  }

  async deleteComment(
    userAuth: UserAuthDto,
    deleteComment: DeleteCommentReqDto,
  ) {
    const deleteTest = deleteComment.commentIds.map(
      (id) => new mongoose.Types.ObjectId(id),
    );
    const comments = await this.commentModel
      .find({
        _id: {
          $in: deleteTest,
        },
      })
      .exec();

    const deleteIds = [];

    comments.forEach((comment) => {
      if (comment.userId == userAuth.userId) deleteIds.push(comment._id);
    });

    await this.commentModel
      .deleteMany({
        _id: { $in: deleteIds.map((id) => new mongoose.Types.ObjectId(id)) },
      })
      .exec();
  }

  async patchComment(
    userAuth: UserAuthDto,
    commentId: string,
    comment: PatchCommentReqDto,
  ) {
    const saveComment = await this.commentModel.findById(commentId);

    if (userAuth.userId != saveComment.userId)
      throw new HttpException('권한 없음', HttpStatus.UNAUTHORIZED);
    saveComment.comment = comment.comment;
    saveComment.save();
  }

  async createComment(
    userAuth: UserAuthDto,
    createComment: CreateCommentReqDto,
  ) {
    const comment = await this.commentModel.create({
      userId: userAuth.userId,
      comment: createComment.comment,
      videoId: createComment.videoId,
    });

    comment.save();
  }

  async getVideoComment(videoId: string, pageNumber: number) {
    const comments: any = await this.commentPageModel.paginate(
      {
        videoId: videoId,
      },
      {
        sort: { createdAt: -1 },
        limit: 16,
        page: pageNumber + 1,
      },
    );

    const usersIds = comments.docs.map((element) => element.userId);

    const users = await this.restApiService.getUser(usersIds);

    const userMap = new Map<number, any>();

    users.forEach((user) => {
      const { userId, ...next } = user;
      userMap.set(userId, next);
    });

    return comments.docs.map(
      (comment) =>
        new CommentListResDto(
          comment._id,
          comment.comment,
          comment.videoId,
          comment.createdAt,
          comment.userId,
          userMap.get(comment.userId),
        ),
    );
  }
}
